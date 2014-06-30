package com.wangyin.cds.server.modules.monitor;

import com.wangyin.cds.server.modules.monitor.dto.RestFulDTO;
import com.wangyin.cds.server.persistence.DbEventDAO;
import com.wangyin.cds.server.persistence.model.DbEvent;
import com.wangyin.cds.server.persistence.templete.IAction;
import com.wangyin.cds.server.persistence.templete.Operation;
import com.wangyin.cds.server.persistence.templete.ResultInfo;
import org.apache.ibatis.session.SqlSession;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
	public RestFulDTO<List<DbEvent>> getEvents(@PathParam("ip") final String ip){
		RestFulDTO<List<DbEvent>> restFulDTO = new RestFulDTO<List<DbEvent>>();
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) {
				DbEventDAO dbEventDAO = session
						.getMapper(DbEventDAO.class);
				DbEvent dbEvent = new DbEvent();
				dbEvent.setIp(ip);
				return dbEventDAO.query(dbEvent);
			}
		});
		if (resultInfo.isSuccess()) {
			restFulDTO.setErrorCode(0);
			restFulDTO.setResultInfo((List<DbEvent>) resultInfo.getResult());
		} else {
			restFulDTO.setErrorCode(-1);
			restFulDTO.setErrMsg(resultInfo.getMessage());
			logger.error("getEvents", resultInfo.getMessage());
		}
//		String tempStr = "{\"errorCode\" : \"0\",\"errMsg\" : \"\",\"resultInfo\":[{ \"eventType\" : \"MONITOR_START\",\"eventId\" : \"S1401088157636\",\"dbInfo\" : { \"groupId\" : \"11\",\"groupName\" : null,\"dbIp\" : \"10.9.3.10\",\"dbName\" : \"test\",\"dbType\" : \"Mysql\",\"masterOrSlave\" : \"Master\",\"dbPort\" : \"3306\"},\"monitorId\" :1}]}";
		return restFulDTO;
		
	}
}
