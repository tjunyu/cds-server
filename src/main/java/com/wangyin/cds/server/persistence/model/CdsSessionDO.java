package com.wangyin.cds.server.persistence.model;

import java.util.Date;
import java.io.Serializable;

/**   
 * @author wy   
 */
public class CdsSessionDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7818407307080110060L;
	private Integer id;
	private String sessionId;
	private Date createdTime;
	private Date lastAccessTime;
	private String status;
	private String attributes;

	public Integer getId() {
 		return this.id;
	}
	
	public void setId(Integer id) {
 		this.id = id;
	}

	public String getSessionId() {
 		return this.sessionId;
	}
	
	public void setSessionId(String sessionId) {
 		this.sessionId = sessionId;
	}

	public Date getCreatedTime() {
 		return this.createdTime;
	}
	
	public void setCreatedTime(Date createdTime) {
 		this.createdTime = createdTime;
	}

	public Date getLastAccessTime() {
 		return this.lastAccessTime;
	}
	
	public void setLastAccessTime(Date lastAccessTime) {
 		this.lastAccessTime = lastAccessTime;
	}

	public String getStatus() {
 		return this.status;
	}
	
	public void setStatus(String status) {
 		this.status = status;
	}

	public String getAttributes() {
 		return this.attributes;
	}
	
	public void setAttributes(String attributes) {
 		this.attributes = attributes;
	}
}
