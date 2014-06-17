package com.wangyin.cds.server.container.sample;

public interface IHelloMBean {
	void setMessage(String msg);
	String getMessage();
	int getCount();
	void setCount(int count);
}
