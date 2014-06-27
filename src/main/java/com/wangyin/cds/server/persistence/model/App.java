package com.wangyin.cds.server.persistence.model;

import java.util.Date;
import java.io.Serializable;

/**
 * wy
 */
public class App implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5534527797461561003L;
	private Integer id;
	private String appName;
	private String appKey;
	private String owner;
	private String phone;
	private String email;
	private String attribute;
	private String createBy;
	private Date creationDate;
	private String modifiedBy;
	private Date modificationDate;
	private String deleteStatus;

	public Integer getId() {
 		return this.id;
	}
	
	public void setId(Integer id) {
 		this.id = id;
	}

	public String getAppName() {
 		return this.appName;
	}
	
	public void setAppName(String appName) {
 		this.appName = appName;
	}

	public String getAppKey() {
 		return this.appKey;
	}
	
	public void setAppKey(String appKey) {
 		this.appKey = appKey;
	}

	public String getOwner() {
 		return this.owner;
	}
	
	public void setOwner(String owner) {
 		this.owner = owner;
	}

	public String getPhone() {
 		return this.phone;
	}
	
	public void setPhone(String phone) {
 		this.phone = phone;
	}

	public String getEmail() {
 		return this.email;
	}
	
	public void setEmail(String email) {
 		this.email = email;
	}

	public String getAttribute() {
 		return this.attribute;
	}
	
	public void setAttribute(String attribute) {
 		this.attribute = attribute;
	}

	public String getCreateBy() {
 		return this.createBy;
	}
	
	public void setCreateBy(String createBy) {
 		this.createBy = createBy;
	}

	public Date getCreationDate() {
 		return this.creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
 		this.creationDate = creationDate;
	}

	public String getModifiedBy() {
 		return this.modifiedBy;
	}
	
	public void setModifiedBy(String modifiedBy) {
 		this.modifiedBy = modifiedBy;
	}

	public Date getModificationDate() {
 		return this.modificationDate;
	}
	
	public void setModificationDate(Date modificationDate) {
 		this.modificationDate = modificationDate;
	}

	public String getDeleteStatus() {
 		return this.deleteStatus;
	}
	
	public void setDeleteStatus(String deleteStatus) {
 		this.deleteStatus = deleteStatus;
	}
}
