package com.wangyin.cds.server.persistence.model;

import java.util.Date;
import java.io.Serializable;

/**   
 * @author wy   
 */
public class DbMonitor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer dbMonitorGroupId;
	private String createBy;
	private Date creationDate;
	private String modifiedBy;
	private Date modificationDate;
	private String deleteStatus;
	private String monitorItem;
	private String monitorItemDesc;
	private String monitorScriptType;
	private String monitorScriptPath;
	private Integer checkInterval;
	private Integer errorNumUpper;
	private Integer checkTimes;
	private Integer thresholdUpper;
	private Integer thresholdLower;
	private Double power;

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

	public String getMonitorItem() {
 		return this.monitorItem;
	}
	
	public void setMonitorItem(String monitorItem) {
 		this.monitorItem = monitorItem;
	}

	public String getMonitorItemDesc() {
 		return this.monitorItemDesc;
	}
	
	public void setMonitorItemDesc(String monitorItemDesc) {
 		this.monitorItemDesc = monitorItemDesc;
	}

	public String getMonitorScriptType() {
 		return this.monitorScriptType;
	}
	
	public void setMonitorScriptType(String monitorScriptType) {
 		this.monitorScriptType = monitorScriptType;
	}

	public String getMonitorScriptPath() {
 		return this.monitorScriptPath;
	}
	
	public void setMonitorScriptPath(String monitorScriptPath) {
 		this.monitorScriptPath = monitorScriptPath;
	}

	public Integer getCheckInterval() {
 		return this.checkInterval;
	}
	
	public void setCheckInterval(Integer checkInterval) {
 		this.checkInterval = checkInterval;
	}

	public Integer getErrorNumUpper() {
 		return this.errorNumUpper;
	}
	
	public void setErrorNumUpper(Integer errorNumUpper) {
 		this.errorNumUpper = errorNumUpper;
	}

	public Integer getCheckTimes() {
 		return this.checkTimes;
	}
	
	public void setCheckTimes(Integer checkTimes) {
 		this.checkTimes = checkTimes;
	}

	public Integer getThresholdUpper() {
 		return this.thresholdUpper;
	}
	
	public void setThresholdUpper(Integer thresholdUpper) {
 		this.thresholdUpper = thresholdUpper;
	}

	public Integer getThresholdLower() {
 		return this.thresholdLower;
	}
	
	public void setThresholdLower(Integer thresholdLower) {
 		this.thresholdLower = thresholdLower;
	}

	public Double getPower() {
 		return this.power;
	}
	
	public void setPower(Double power) {
 		this.power = power;
	}
}
