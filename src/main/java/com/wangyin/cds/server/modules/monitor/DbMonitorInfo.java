package com.wangyin.cds.server.modules.monitor;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.ibatis.session.SqlSession;

import com.wangyin.cds.server.modules.monitor.dto.RestFulDTO;
import com.wangyin.cds.server.persistence.DbMonitorDAO;
import com.wangyin.cds.server.persistence.DbMonitorInstanceDAO;
import com.wangyin.cds.server.persistence.model.DbMonitor;
import com.wangyin.cds.server.persistence.model.DbMonitorInstance;
import com.wangyin.cds.server.persistence.templete.IAction;
import com.wangyin.cds.server.persistence.templete.Operation;
import com.wangyin.cds.server.persistence.templete.ResultInfo;

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
	public RestFulDTO<DbMonitor> getDbMonitorInfo(
			@PathParam("monitorId") final int monitorId) {
		RestFulDTO<DbMonitor> restFulDTO = new RestFulDTO<DbMonitor>();
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) {
				DbMonitorDAO dbMonitorDAO = session
						.getMapper(DbMonitorDAO.class);
				return dbMonitorDAO.load(monitorId);
			}
		});
		if (resultInfo.isSuccess()) {
			restFulDTO.setErrorCode(0);
			restFulDTO.setResultInfo((DbMonitor) resultInfo.getResult());
		} else {
			restFulDTO.setErrorCode(-1);
			restFulDTO.setErrMsg(resultInfo.getMessage());
			logger.error("getDbMonitorInfo", resultInfo.getMessage());
		}
		return restFulDTO;
	}

	@GET
	@Path("dbGroupId/{groupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestFulDTO<List<DbMonitor>> getDbMonitorListByGroupId(
			@PathParam("groupId") final int groupId) {
		RestFulDTO<List<DbMonitor>> restFulDTO = new RestFulDTO<List<DbMonitor>>();
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) {
				DbMonitorDAO dbMonitorDAO = session
						.getMapper(DbMonitorDAO.class);
				DbMonitor dbMonitor = new DbMonitor();
				dbMonitor.setDbMonitorGroupId(groupId);
				return dbMonitorDAO.query(dbMonitor);
			}
		});
		if (resultInfo.isSuccess()) {
			restFulDTO.setErrorCode(0);
			restFulDTO.setResultInfo((List<DbMonitor>) resultInfo.getResult());
		} else {
			restFulDTO.setErrorCode(-1);
			restFulDTO.setErrMsg(resultInfo.getMessage());
			logger.error("getDbMonitorListByGroupId", resultInfo.getMessage());
		}
		return restFulDTO;

	}

	@POST
	@Path("collectMonitorResult/json")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestFulDTO<List<DbMonitor>> collectMonitorResult(
			final DbMonitorInstance dbMonitorInstance) {
		RestFulDTO<List<DbMonitor>> restFulDTO = new RestFulDTO<List<DbMonitor>>();
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) {
				DbMonitorInstanceDAO dbMonitorInstanceDAO = session
						.getMapper(DbMonitorInstanceDAO.class);
				
				DbMonitorDAO dbMonitorDAO = session
						.getMapper(DbMonitorDAO.class);
				
				DbMonitor dbMonitor = dbMonitorDAO.load(dbMonitorInstance.getDbMinitorId());
				
				if(dbMonitorInstance.getErrorNum()>dbMonitor.getErrorNumUpper()){
					dbMonitorInstance.setStatus(DbMonitor.CRITICALl);
				}else{
					if(dbMonitorInstance.getMonitorValue()<=dbMonitor.getThresholdLower()){
						dbMonitorInstance.setStatus(DbMonitor.OK);
						dbMonitorInstance.setAlarmMsg("监控指标值："+dbMonitorInstance.getMonitorValue()+"，正常");
					}else if(dbMonitorInstance.getMonitorValue()>dbMonitor.getThresholdLower()&&dbMonitorInstance.getMonitorValue()<=dbMonitor.getThresholdUpper()){
						dbMonitorInstance.setStatus(DbMonitor.WARNING);
						dbMonitorInstance.setAlarmMsg("监控指标值："+dbMonitorInstance.getMonitorValue()+"，警告");
					}
				}
				dbMonitorInstance.setIntegral(1);//积分
				dbMonitorInstanceDAO.insert(dbMonitorInstance);
				return null;
			}
		});
		if (resultInfo.isSuccess()) {
			restFulDTO.setErrorCode(0);
		} else {
			restFulDTO.setErrorCode(-1);
			restFulDTO.setErrMsg(resultInfo.getMessage());
			logger.error("collectMonitorResult", resultInfo.getMessage());
		}
		return restFulDTO;

	}

}
