package com.wangyin.cds.server.persistence.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
/**   
 * @author wy   
 */
@XmlRootElement
public class DbMonitorInstance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7888947095277539194L;
	private Integer id;
	private Integer dbMinitorId;
	private Date creationDate;
	private String monitorItem;
	private String monitorItemName;
	private String status;
	private Integer monitorValue;
	private Integer integral;
	private Integer errorNum;
	private String alarmMsg;

	public Integer getId() {
 		return this.id;
	}
	
	public void setId(Integer id) {
 		this.id = id;
	}

	public Integer getDbMinitorId() {
 		return this.dbMinitorId;
	}
	
	public void setDbMinitorId(Integer dbMinitorId) {
 		this.dbMinitorId = dbMinitorId;
	}

	public Date getCreationDate() {
 		return this.creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
 		this.creationDate = creationDate;
	}

	public String getMonitorItem() {
 		return this.monitorItem;
	}
	
	public void setMonitorItem(String monitorItem) {
 		this.monitorItem = monitorItem;
	}

	public String getMonitorItemName() {
 		return this.monitorItemName;
	}
	
	public void setMonitorItemName(String monitorItemName) {
 		this.monitorItemName = monitorItemName;
	}

	public String getStatus() {
 		return this.status;
	}
	
	public void setStatus(String status) {
 		this.status = status;
	}

	public Integer getMonitorValue() {
 		return this.monitorValue;
	}
	
	public void setMonitorValue(Integer monitorValue) {
 		this.monitorValue = monitorValue;
	}

	public Integer getIntegral() {
 		return this.integral;
	}
	
	public void setIntegral(Integer integral) {
 		this.integral = integral;
	}

	public Integer getErrorNum() {
 		return this.errorNum;
	}
	
	public void setErrorNum(Integer errorNum) {
 		this.errorNum = errorNum;
	}

	public String getAlarmMsg() {
 		return this.alarmMsg;
	}
	
	public void setAlarmMsg(String alarmMsg) {
 		this.alarmMsg = alarmMsg;
	}
}
