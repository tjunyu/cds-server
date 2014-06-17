package com.wangyin.cds.server.modules.admin;

import javax.ws.rs.core.Application;

import com.wangyin.cds.server.modules.IPrivateModule;
import com.wangyin.cds.server.modules.RestfulModule;

/**
 * 管控模块
 * @author david
 *
 */
public class AdminModule extends RestfulModule implements IPrivateModule {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(AdminModule.class);

	@Override
	public void configure(Application application) {
		
	}
}
