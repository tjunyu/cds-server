package com.wangyin.cds.server.container.aa;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.glassfish.hk2.api.Factory;

import com.wangyin.cds.server.Predefined;

public class SessionHolder implements Factory<ICdsSession> {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(SessionHolder.class);
	@Context
	Application app;
	@Context
	HttpHeaders headers;
	
	public ICdsSession provide() {
		String session_id = headers.getHeaderString(Predefined.HTTP_HEAD_SESSION);
		SessionManager manager= (SessionManager) app.getProperties().get(Predefined.PROP_SESSION_MGR);
		return manager.getSessionById(session_id);
	}

	public void dispose(ICdsSession instance) {
		
	}

}
