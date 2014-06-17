package com.wangyin.cds.server.modules;

import java.util.Map;

import com.wangyin.cds.server.NodeEvent;
import com.wangyin.cds.server.ServerNode;

public interface INodeModule {
	String getName();
	ServerNode getHostNode();
	void recieveEvent(NodeEvent event);
	void sendEvent(NodeEvent event);
	void configure(Map<String,Object> configuration);
	void startup() throws Exception;
	void shutdown() throws Exception;
}
