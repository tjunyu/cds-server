package com.wangyin.cds.server.modules.monitor;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.ibatis.session.SqlSession;

import com.wangyin.cds.server.modules.monitor.dto.RestFulDTO;
import com.wangyin.cds.server.persistence.DbInfoDAO;
import com.wangyin.cds.server.persistence.PersistenceManager;
import com.wangyin.cds.server.persistence.RdbinfodbgroupDAO;
import com.wangyin.cds.server.persistence.model.DbInfo;
import com.wangyin.cds.server.persistence.model.Rdbinfodbgroup;

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
//			DbGroupDAO dbGroupDAO = session.getMapper(DbGroupDAO.class);
//			DbGroup dbGroup = dbGroupDAO.load(dbGroupId);
			RdbinfodbgroupDAO rdbinfodbgroupDAO = session.getMapper(RdbinfodbgroupDAO.class);
			Rdbinfodbgroup rdbinfodbgroup = new Rdbinfodbgroup();
			rdbinfodbgroup.setDbGroupId(dbGroupId);
			List<Rdbinfodbgroup> rdbinfodbgroupList = rdbinfodbgroupDAO.query(rdbinfodbgroup);
			DbInfoDAO dbInfoDAO = session.getMapper(DbInfoDAO.class);
			dbInfoList = new ArrayList<DbInfo>(rdbinfodbgroupList.size());
			for(Rdbinfodbgroup rdbig:rdbinfodbgroupList){
				dbInfoList.add(dbInfoDAO.load(rdbig.getDbInfoId()));//type不用穿
			}
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
