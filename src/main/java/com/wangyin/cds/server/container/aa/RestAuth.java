package com.wangyin.cds.server.container.aa;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.wangyin.cds.server.Predefined;
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
		}
	}
	//TODO 应用登录
	private ICdsSession tryLogin(int appId, String appKey) {
		if (appId==12345 && "hellokey".equals(appKey)){
			SessionManager sm = (SessionManager) app.getProperties().get(Predefined.PROP_SESSION_MGR);
			return sm.createSession();
		}else{
			return null;
		}
	}
}
