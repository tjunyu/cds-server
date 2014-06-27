package com.wangyin.cds.server.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.map.ObjectMapper;

import com.wangyin.cds.server.persistence.CdsSessionDAO;
import com.wangyin.cds.server.persistence.model.CdsSessionDO;
import com.wangyin.cds.server.persistence.templete.IAction;
import com.wangyin.cds.server.persistence.templete.Operation;
import com.wangyin.cds.server.persistence.templete.ResultInfo;
/**   
 * @author wy   session 管理，session的序列化，反序列化
 */
public final class SessionManager {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(SessionManager.class);
	static final int STATUS_CREATED=0, STATUS_NORMAL=1,STATUS_NEED_UPDATE=2, STATUS_NEED_DELETE=3;
	private int maxSessionCount;
	private long maxSessionPeriod;//inMS
	private int gcScanInterval=5; //in Seconds;
	private int flushInterval=10; //in Seconds;
	private ObjectMapper mapper = new ObjectMapper();
	private Map<String, CdsSession> sessions = new ConcurrentHashMap<String, CdsSession>();
	private Thread scanThread, flushThread;
	public SessionManager() {
		maxSessionCount = 1000;
		maxSessionPeriod  = 30*60*1000;
		restoreSessions();
		startScanThread();
		startFlushThread();
	}
	public ICdsSession getSessionById(String id){
		if (id ==null) return null;
		return sessions.get(id);
	}
	private void startFlushThread() {
		flushThread = new Thread("Flush Session Thread"){
			@Override
			public void run() {
				while (true){
					try {
						Thread.sleep(flushInterval*1000);
					} catch (InterruptedException e) {
						break;
					}
					flushSessions();
				}
				
			}
		};
		flushThread.setDaemon(true);
		flushThread.start();
	}
	private void startScanThread() {
		scanThread = new Thread("Scan Session Thread"){
			@Override
			public void run() {
				while (true){
					try {
						Thread.sleep(gcScanInterval*1000);
					} catch (InterruptedException e) {
						break;
					}
					defragSessions();
				}
			}
		};
		scanThread.setDaemon(true);
		scanThread.start();
	}
	/**
	 * 
	 * @return null if it reaches the upper limit
	 */
	public ICdsSession createSession(){
		if (sessions.size() >= maxSessionCount){
			return null;
		}
		CdsSession sess = new CdsSession(makeSessionId());
		sessions.put(sess.getId(), sess);
		return sess;
	}
	public void close(){
		scanThread.interrupt();
		flushThread.interrupt();
	}
	private String makeSessionId(){
		return UUID.randomUUID().toString();
	}
	/**
	 * 从数据库中恢复session
	 */
	private void restoreSessions(){
		Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) {
				CdsSessionDAO cdsSessionDAO = session.getMapper(CdsSessionDAO.class);
				int totalSession = cdsSessionDAO.count();
				int totalpage = totalSession/1000+1;
				for(int i =1;i<=totalpage;i++){
					List<CdsSessionDO> sessionList = cdsSessionDAO.getPageList((i-1)*1000, 1000);
					for(CdsSessionDO cdsSessionDO:sessionList){
						CdsSession cdsSession = new CdsSession(cdsSessionDO.getSessionId(),cdsSessionDO.getCreatedTime().getTime(),cdsSessionDO.getLastAccessTime().getTime());
						sessions.put(cdsSessionDO.getSessionId(), cdsSession);
					}
				}
				return null;
			}
		});
	}
	/**
	 * 将session刷新到数据库中
	 */
	private void flushSessions(){
		flushSessions(sessions.values());
	}
	private void flushSessions(Collection<CdsSession> values) {
		for (CdsSession sess : values){
			if (sess.getStatus() == STATUS_CREATED){
				insert(sess);
			}else if (sess.getStatus() == STATUS_NEED_UPDATE){
				updateSession(sess);
				sess.setStatus(STATUS_NORMAL);
			}else if(sess.getStatus() == STATUS_NEED_DELETE){
				deleteSession(sess);
			}
		}
	}
	private void insert(final CdsSession sess) {
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) throws Exception {
				CdsSessionDAO cdsSessionDAO = session.getMapper(CdsSessionDAO.class);
				CdsSessionDO cdsSessionDO = new CdsSessionDO(); 
				cdsSessionDO.setSessionId(sess.getId());
				cdsSessionDO.setCreatedTime(new Date(sess.getCreatedTime()));
				cdsSessionDO.setLastAccessTime(new Date(sess.getLastAccessTime()));
				cdsSessionDO.setAttributes(mapper.writeValueAsString(sess.getAttributes()));
				cdsSessionDAO.insert(cdsSessionDO);
				return null;
			}
		});
		if(resultInfo.isSuccess()){
			sess.setStatus(STATUS_NORMAL);
		}
	}
	//在数据库删除session
	private void deleteSession(final CdsSession sess) {
		Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) throws Exception {
				CdsSessionDAO cdsSessionDAO = session.getMapper(CdsSessionDAO.class);
				cdsSessionDAO.delete(sess.getId());
				return null;
			}
		});
	}
	//在数据库中更新session
	private void updateSession(final CdsSession sess) {
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) throws Exception {
				CdsSessionDAO cdsSessionDAO = session.getMapper(CdsSessionDAO.class);
				CdsSessionDO cdsSessionDO = new CdsSessionDO(); 
				cdsSessionDO.setSessionId(sess.getId());
				cdsSessionDO.setCreatedTime(new Date(sess.getCreatedTime()));
				cdsSessionDO.setLastAccessTime(new Date(sess.getLastAccessTime()));
				cdsSessionDO.setAttributes(mapper.writeValueAsString(sess.getAttributes()));
				cdsSessionDAO.update(cdsSessionDO);
				return null;
			}
		});
		if(resultInfo.isSuccess()){
			sess.setStatus(STATUS_NORMAL);
		}
	}
	/**
	 * 整理session，将失效的session清除
	 */
	private void defragSessions(){
		List<CdsSession> removed = new ArrayList<CdsSession>();
		for (Iterator<Entry<String, CdsSession>> siter= sessions.entrySet().iterator(); siter.hasNext();){
			CdsSession s = siter.next().getValue();
			if(s.exceedDuration(maxSessionPeriod)){
				s.setStatus(STATUS_NEED_DELETE);
				removed.add(s);
				siter.remove();
			}
		}
		flushSessions(removed);
	}
}
