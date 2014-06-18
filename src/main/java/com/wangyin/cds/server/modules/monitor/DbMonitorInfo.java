package com.wangyin.cds.server.modules.monitor;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.ibatis.session.SqlSession;

import com.wangyin.cds.server.persistence.DbMonitorDAO;
import com.wangyin.cds.server.persistence.PersistenceManager;
import com.wangyin.cds.server.persistence.model.DbMonitor;

/**   
 * @author wy   
 */
@Path("monitor")
public class DbMonitorInfo {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(DbMonitorInfo.class);
	@GET
	@Path("monitorId/{monitorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public DbMonitor getDbMonitorInfo(@PathParam("monitorId")int monitorId){
		SqlSession session = PersistenceManager.getSession().openSession();
		DbMonitor dbMonitor = null;
		try {
			DbMonitorDAO dbMonitorDAO = session
					.getMapper(DbMonitorDAO.class);
			dbMonitor = dbMonitorDAO.load(monitorId);
		} catch(Exception e){
			logger.error("getDbMonitorInfo", e);
		}finally {
			session.close();
		}
		return dbMonitor;
	}

	@GET
	@Path("groupId/{groupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DbMonitor> getDbMonitorListByGroupId(@PathParam("groupId")int groupId){
		SqlSession session = PersistenceManager.getSession().openSession();
		List<DbMonitor> dbMonitorList = null;
		try {
			DbMonitorDAO dbMonitorDAO = session
					.getMapper(DbMonitorDAO.class);
			DbMonitor dbMonitor = new DbMonitor();
			dbMonitor.setDbGroupId(groupId);
			dbMonitorList = dbMonitorDAO.query(dbMonitor);
		} catch(Exception e){
			logger.error("getDbMonitorInfo", e);
		}finally {
			session.close();
		}
		return dbMonitorList;
		
	}
}
