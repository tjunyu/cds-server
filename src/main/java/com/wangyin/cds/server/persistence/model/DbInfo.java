package com.wangyin.cds.server.persistence.model;

import java.util.Date;
import java.io.Serializable;

/**   
 * @author wy   
 */
public class DbInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4751138252604857037L;
	private Integer id;
	private Integer dbMonitorGroupId;
	private String ip;
	private Integer port;
	private Integer dbServerId;
	private String createBy;
	private Date creationDate;
	private String modifiedBy;
	private Date modificationDate;
	private String deleteStatus;
	private String dbType;
	private String masterOrSlave;

	public Integer getId() {
 		return this.id;
	}
	
	public void setId(Integer id) {
 		this.id = id;
	}

	public Integer getDbMonitorGroupId() {
 		return this.dbMonitorGroupId;
	}
	
	public void setDbMonitorGroupId(Integer dbMonitorGroupId) {
 		this.dbMonitorGroupId = dbMonitorGroupId;
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
}
