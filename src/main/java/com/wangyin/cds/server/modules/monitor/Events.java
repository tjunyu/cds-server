package com.wangyin.cds.server.modules.monitor;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.ibatis.session.SqlSession;

import com.wangyin.cds.server.modules.monitor.dto.EventDTO;
import com.wangyin.cds.server.modules.monitor.dto.RestFulDTO;
import com.wangyin.cds.server.persistence.PersistenceManager;
import com.wangyin.cds.server.persistence.model.DbInfo;

/**   
 * @author wy   
 */
@Path("events")
public class Events {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(Events.class);
	

	@GET
	@Path("ip/{ip}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDbInfoListByType(@PathParam("ip")String ip){
		SqlSession session = PersistenceManager.getSession().openSession();
		RestFulDTO<EventDTO> restFulDTO = new RestFulDTO<EventDTO>();
		List<DbInfo> dbInfoList = null;
//		try {
//			DbGroupDAO dbGroupDAO = session.getMapper(DbGroupDAO.class);
//			DbGroup dbGroup = dbGroupDAO.load(dbGroupId);
//			RdbinfodbgroupDAO rdbinfodbgroupDAO = session.getMapper(RdbinfodbgroupDAO.class);
//			Rdbinfodbgroup rdbinfodbgroup = new Rdbinfodbgroup();
//			rdbinfodbgroup.setDbGroupId(dbGroupId);
//			List<Rdbinfodbgroup> rdbinfodbgroupList = rdbinfodbgroupDAO.query(rdbinfodbgroup);
//			DbInfoDAO dbInfoDAO = session.getMapper(DbInfoDAO.class);
//			dbInfoList = new ArrayList<DbInfo>(rdbinfodbgroupList.size());
//			for(Rdbinfodbgroup rdbig:rdbinfodbgroupList){
//				dbInfoList.add(dbInfoDAO.load(rdbig.getDbInfoId()));//type不用穿
//			}
//			restFulDTO.setResultInfo(dbInfoList);
//		} catch(Exception e){
//			restFulDTO.setErrorCode("error");
//			restFulDTO.setErrMsg(e.toString());
//			logger.error("getDbMonitorInfo", e);
//		}finally {
//			session.close();
//		}
		String tempStr = "{\"errorCode\" : \"0\",\"errMsg\" : \"\",\"resultInfo\":[{ \"eventType\" : \"MONITOR_START\",\"eventId\" : \"S1401088157636\",\"dbInfo\" : { \"groupId\" : \"11\",\"groupName\" : null,\"dbIp\" : \"10.9.3.10\",\"dbName\" : \"test\",\"dbType\" : \"Mysql\",\"masterOrSlave\" : \"Master\",\"dbPort\" : \"3306\"},\"monitorId\" :1}]}";
		return tempStr;
		
	}
}
