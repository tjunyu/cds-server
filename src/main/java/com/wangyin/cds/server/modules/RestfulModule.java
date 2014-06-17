package com.wangyin.cds.server.modules;

import java.util.Map;

import javax.ws.rs.core.Application;

import com.wangyin.cds.server.container.NettyContainerInitializer;

public abstract class RestfulModule extends ServerModule {
	public abstract void configure(Application application);
	public void configure(Map<String, Object> configuration) {
		Application app = (Application) configuration.get(NettyContainerInitializer.PROP_APPLICATION);
		assert(app != null);
		configure(app);
	}

	public void startup() throws Exception {
		//Nothing to do 
	}

	public void shutdown() throws Exception {
		//Nothing to do
	}

}
