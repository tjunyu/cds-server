package com.wangyin.cds.server.modules;

import com.wangyin.cds.server.NodeEvent;
import com.wangyin.cds.server.ServerNode;

public abstract class ServerModule implements INodeModule {
	private String name;
	private ServerNode hostNode;
	public void setHostNode(ServerNode hostNode) {
		this.hostNode = hostNode;
	}
	public ServerNode getHostNode() {
		return hostNode;
	}
	public void recieveEvent(NodeEvent event) {
		
	}
	public void sendEvent(NodeEvent event) {
		hostNode.sendEvent(event);		
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
