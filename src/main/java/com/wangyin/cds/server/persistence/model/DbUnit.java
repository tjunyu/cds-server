package com.wangyin.cds.server.persistence.model;

import java.util.Date;
import java.io.Serializable;

/**   
 * @author wy   
 */
public class DbUnit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 227593456682529758L;
	private Integer id;
	private Integer dbHostGroupId;
	private String ip;
	private Integer port;
	private Integer dbServerId;
	private String dbName;
	private String passwd;
	private String dbType;
	private String masterOrSlave;
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

	public Integer getDbHostGroupId() {
 		return this.dbHostGroupId;
	}
	
	public void setDbHostGroupId(Integer dbHostGroupId) {
 		this.dbHostGroupId = dbHostGroupId;
	}

	public String getIp() {
 		return this.ip;
	}
	
	public void setIp(String ip) {
 		this.ip = ip;
	}

	public Integer getPort() {
 		return this.port;
	}
	
	public void setPort(Integer port) {
 		this.port = port;
	}

	public Integer getDbServerId() {
 		return this.dbServerId;
	}
	
	public void setDbServerId(Integer dbServerId) {
 		this.dbServerId = dbServerId;
	}

	public String getDbName() {
 		return this.dbName;
	}
	
	public void setDbName(String dbName) {
 		this.dbName = dbName;
	}

	public String getPasswd() {
 		return this.passwd;
	}
	
	public void setPasswd(String passwd) {
 		this.passwd = passwd;
	}

	public String getDbType() {
 		return this.dbType;
	}
	
	public void setDbType(String dbType) {
 		this.dbType = dbType;
	}

	public String getMasterOrSlave() {
 		return this.masterOrSlave;
	}
	
	public void setMasterOrSlave(String masterOrSlave) {
 		this.masterOrSlave = masterOrSlave;
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
