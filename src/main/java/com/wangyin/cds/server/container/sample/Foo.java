package com.wangyin.cds.server.container.sample;

import java.io.Serializable;
import java.util.Date;


public class Foo implements Serializable {

	private static final long serialVersionUID = 1403563701749941983L;
	String name;
	Date date;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
