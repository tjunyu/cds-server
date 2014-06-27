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
import com.wangyin.cds.server.persistence.model.DbInfo;
import com.wangyin.cds.server.persistence.model.DbUnit;
import com.wangyin.cds.server.persistence.templete.IAction;
import com.wangyin.cds.server.persistence.templete.Operation;
import com.wangyin.cds.server.persistence.templete.ResultInfo;

/**
 * @author wy
 */
@Path("dbinfo")
public class Dbinfo {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(Dbinfo.class);

	@SuppressWarnings("unchecked")
	@GET
	@Path("dbGroupId/{dbGroupId}/type/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestFulDTO<List<DbInfo>> getDbInfoListByGroupId(
			@PathParam("dbGroupId") final int dbGroupId,
			@PathParam("type") final String type) {
		RestFulDTO<List<DbInfo>> restFulDTO = new RestFulDTO<List<DbInfo>>();
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) {
				DbInfoDAO dbInfoDAO = session.getMapper(DbInfoDAO.class);
				DbInfo dbInfo = new DbInfo();
				dbInfo.setDbMonitorGroupId(dbGroupId);
				dbInfo.setMasterOrSlave(type);
				return dbInfoDAO.query(dbInfo);
			}
		});
		if (resultInfo.isSuccess()) {
			restFulDTO.setErrorCode(0);
			restFulDTO.setResultInfo((List<DbInfo>) resultInfo.getResult());
		} else {
			restFulDTO.setErrorCode(-1);
			restFulDTO.setErrMsg(resultInfo.getMessage());
			logger.error("getDbInfoListByGroupId", resultInfo.getMessage());
		}
		return restFulDTO;

	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("dbunits/{ip}/dbtype/{dbType}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestFulDTO<List<DbUnit>> getDbunitList(
			@PathParam("ip") final String ip,
			@PathParam("dbType") final String dbType) {
		RestFulDTO<List<DbUnit>> restFulDTO = new RestFulDTO<List<DbUnit>>();
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) {
				DbUnitDAO dbUnitDAO = session.getMapper(DbUnitDAO.class);
				DbUnit dbUnit = new DbUnit();
				dbUnit.setDbType(dbType);
				dbUnit.setIp(ip);
				return dbUnitDAO.query(dbUnit);
			}
		});
		if (resultInfo.isSuccess()) {
			restFulDTO.setErrorCode(0);
			restFulDTO.setResultInfo((List<DbUnit>) resultInfo.getResult());
		} else {
			restFulDTO.setErrorCode(-1);
			restFulDTO.setErrMsg(resultInfo.getMessage());
			logger.error("getDbunitList", resultInfo.getMessage());
		}
		return restFulDTO;

	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("dbIp/{dbIp}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestFulDTO<List<DbInfo>> getDbInfoListByDbIp(
			@PathParam("dbIp") final String dbIp) {
		RestFulDTO<List<DbInfo>> restFulDTO = new RestFulDTO<List<DbInfo>>();
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) {
				DbInfoDAO dbInfoDAO = session.getMapper(DbInfoDAO.class);
				DbInfo dbInfo = new DbInfo();
				dbInfo.setIp(dbIp);
				return dbInfoDAO.query(dbInfo);
			}
		});
		if (resultInfo.isSuccess()) {
			restFulDTO.setErrorCode(0);
			restFulDTO.setResultInfo((List<DbInfo>) resultInfo.getResult());
		} else {
			restFulDTO.setErrorCode(-1);
			restFulDTO.setErrMsg(resultInfo.getMessage());
			logger.error("getDbInfoListByDbIp", resultInfo.getMessage());
		}
		return restFulDTO;

	}

}
