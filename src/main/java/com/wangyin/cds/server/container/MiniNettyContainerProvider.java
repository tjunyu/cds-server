package com.wangyin.cds.server.container;

import javax.ws.rs.ProcessingException;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.spi.ContainerProvider;

public class MiniNettyContainerProvider implements ContainerProvider {

	public <T> T createContainer(Class<T> type, ApplicationHandler appHandler)
			throws ProcessingException {
		if(type != MiniNettyContainer.class){
			return null;
		}
		return (T) new MiniNettyContainer(appHandler);
	}

}
