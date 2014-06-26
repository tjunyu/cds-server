package com.wangyin.cds.server.modules.monitor;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.ibatis.session.SqlSession;

import com.wangyin.cds.server.modules.monitor.dto.RestFulDTO;
import com.wangyin.cds.server.persistence.DbInfoDAO;
import com.wangyin.cds.server.persistence.DbUnitDAO;
import com.wangyin.cds.server.persistence.PersistenceManager;
import com.wangyin.cds.server.persistence.model.DbInfo;
import com.wangyin.cds.server.persistence.model.DbUnit;

/**   
 * @author wy   
 */
@Path("dbinfo")
public class Dbinfo {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(Dbinfo.class);
	

	@GET
	@Path("dbGroupId/{dbGroupId}/type/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestFulDTO<List<DbInfo>> getDbInfoListByType(@PathParam("dbGroupId")int dbGroupId,@PathParam("type")String type){
		SqlSession session = PersistenceManager.getSession().openSession();
		RestFulDTO<List<DbInfo>> restFulDTO = new RestFulDTO<List<DbInfo>>();
		List<DbInfo> dbInfoList = null;
		try {
			DbInfoDAO dbInfoDAO = session.getMapper(DbInfoDAO.class);
			DbInfo dbInfo = new DbInfo();
			dbInfo.setDbMonitorGroupId(dbGroupId);
			dbInfo.setMasterOrSlave(type);
			dbInfoList = dbInfoDAO.query(dbInfo);
			restFulDTO.setResultInfo(dbInfoList);
		} catch(Exception e){
			restFulDTO.setErrorCode("error");
			restFulDTO.setErrMsg(e.toString());
			logger.error("getDbMonitorInfo", e);
		}finally {
			session.close();
		}
		return restFulDTO;
		
	}
	
	@GET
	@Path("dbunits/{ip}/dbtype/{dbType}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestFulDTO<List<DbUnit>> getDbInfoListByType(@PathParam("ip")String ip,@PathParam("dbType")String dbType){
		SqlSession session = PersistenceManager.getSession().openSession();
		RestFulDTO<List<DbUnit>> restFulDTO = new RestFulDTO<List<DbUnit>>();
		List<DbUnit> dbUnitList = null;
		try {
			DbUnitDAO dbUnitDAO = session.getMapper(DbUnitDAO.class);
			DbUnit dbUnit = new DbUnit();
			dbUnit.setDbType(dbType);
			dbUnit.setIp(ip);
			dbUnitList = dbUnitDAO.query(dbUnit);
			restFulDTO.setResultInfo(dbUnitList);
		} catch(Exception e){
			restFulDTO.setErrorCode("error");
			restFulDTO.setErrMsg(e.toString());
			logger.error("getDbMonitorInfo", e);
		}finally {
			session.close();
		}
		return restFulDTO;
		
	}
	
	@GET
	@Path("dbIp/{dbIp}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestFulDTO<List<DbInfo>> getDbInfoListByDbIp(@PathParam("dbIp")String dbIp){
		SqlSession session = PersistenceManager.getSession().openSession();
		RestFulDTO<List<DbInfo>> restFulDTO = new RestFulDTO<List<DbInfo>>();
		List<DbInfo> dbInfoList = null;
		try {
			DbInfoDAO dbInfoDAO = session.getMapper(DbInfoDAO.class);
			DbInfo dbInfo = new DbInfo();
			dbInfo.setIp(dbIp);
			dbInfoList = dbInfoDAO.query(dbInfo);
			restFulDTO.setResultInfo(dbInfoList);
		} catch(Exception e){
			restFulDTO.setErrorCode("error");
			restFulDTO.setErrMsg(e.toString());
			logger.error("getDbMonitorInfo", e);
		}finally {
			session.close();
		}
		return restFulDTO;
		
	}
	
}
