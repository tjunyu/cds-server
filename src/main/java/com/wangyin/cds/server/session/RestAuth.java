package com.wangyin.cds.server.session;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.ibatis.session.SqlSession;

import com.wangyin.cds.server.Predefined;
import com.wangyin.cds.server.persistence.AppDAO;
import com.wangyin.cds.server.persistence.model.App;
import com.wangyin.cds.server.persistence.templete.IAction;
import com.wangyin.cds.server.persistence.templete.Operation;
import com.wangyin.cds.server.persistence.templete.ResultInfo;
/**   
 * @author wy   session 权限验证
 */
@Provider
@PreMatching
public class RestAuth implements ContainerRequestFilter {
	@Context
	Application app;
	@Context
	HttpHeaders headers;
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		String session_id = headers.getHeaderString(Predefined.HTTP_HEAD_SESSION);
		
		if (session_id == null){
			String app_id_str = headers.getHeaderString(Predefined.HTTP_HEAD_APP_ID);
			String app_key_str = headers.getHeaderString(Predefined.HTTP_HEAD_APP_KEY);
			
			if (app_id_str != null && app_key_str != null){
				ICdsSession login_session = tryLogin(Integer.parseInt(app_id_str),app_key_str);
				if (login_session != null){
					session_id = login_session.getId();
				}
			}
			if (session_id != null){
				requestContext.getHeaders().add(Predefined.HTTP_HEAD_SESSION, session_id);
			}else{
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access this resource").build());
			}
		}else{
			SessionManager sm = (SessionManager) app.getProperties().get(Predefined.PROP_SESSION_MGR);
			sm.getSessionById(session_id).touch();
		}
	}

	private ICdsSession tryLogin(final int appId, final String appKey) {
		ResultInfo resultInfo = Operation.callBack(new IAction() {
			public Object doAction(SqlSession session) {
				AppDAO appDAO = session.getMapper(AppDAO.class);
				App app = new App();
				app.setId(appId);
				app.setAppKey(appKey);
				return appDAO.query(app);
			}
		});
		if(resultInfo.isSuccess()){
			if(resultInfo.getResult()!=null){
				SessionManager sm = (SessionManager) app.getProperties().get(Predefined.PROP_SESSION_MGR);
				return sm.createSession();
			}
		}
	    return null;
	}
}
