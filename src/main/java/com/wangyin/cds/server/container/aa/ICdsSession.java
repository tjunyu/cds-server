package com.wangyin.cds.server.container.aa;

import java.io.Serializable;

public interface ICdsSession extends Serializable{

	public abstract void setId(String id);

	public abstract String getId();

	public abstract void setAttribute(String name, Serializable object);
	public abstract void removeAttribute(String name);
	public abstract Serializable getAttribute(String name);

	//close and remove from server
	public abstract void close();

}