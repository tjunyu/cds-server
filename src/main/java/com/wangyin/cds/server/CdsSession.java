package com.wangyin.cds.server;

import java.io.Serializable;
import java.net.SocketAddress;
/**
 * 集群之间的会话
 * @author david
 *
 */
public class CdsSession implements Serializable {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CdsSession.class);
	private final String id;
	private SocketAddress remoteEndpoint;
	public CdsSession(String id) {
		super();
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void reconnect(){
		logger.info("do reconnecting for session "+id+"");
	}
}
