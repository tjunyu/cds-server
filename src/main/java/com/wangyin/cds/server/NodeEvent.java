package com.wangyin.cds.server;

import java.util.EventObject;

public class NodeEvent extends EventObject {

	private int code;
	
	public NodeEvent(Object source) {
		super(source);
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	private static final long serialVersionUID = -8788303610436219283L;

}
