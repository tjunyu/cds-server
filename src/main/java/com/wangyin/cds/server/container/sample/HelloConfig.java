package com.wangyin.cds.server.container.sample;

public class HelloConfig implements IHelloMBean {
	private String message;
	private int count;
	public void setMessage(String message) {
		this.message = message;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMessage() {
		return this.message;
	}

	public int getCount() {
		return this.count;
	}
	

}
