package com.wangyin.cds.server.persistence.model;

import java.util.Date;
import java.io.Serializable;

/**   
 * @author wy   
 */
public class DbMonitorGroup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7373609197506125126L;
	private Integer id;
	private String groupName;
	private String createBy;
	private Date creationDate;
	private String modifiedBy;
	private Date modificationDate;
	private String deleteStatus;
	private String dbType;

	public Integer getId() {
 		return this.id;
	}
	
	public void setId(Integer id) {
 		this.id = id;
	}

	public String getGroupName() {
 		return this.groupName;
	}
	
	public void setGroupName(String groupName) {
 		this.groupName = groupName;
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
}
