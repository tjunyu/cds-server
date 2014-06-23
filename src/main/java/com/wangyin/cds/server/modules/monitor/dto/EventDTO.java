package com.wangyin.cds.server.modules.monitor.dto;

import com.wangyin.cds.server.persistence.model.DbInfo;

/**   
 * @author wy   
 */
public class EventDTO {
	
	private String eventType;
	private String eventId;
	private DbInfo dbInfo;
	private String monitorIds;
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public DbInfo getDbInfo() {
		return dbInfo;
	}
	public void setDbInfo(DbInfo dbInfo) {
		this.dbInfo = dbInfo;
	}
	public String getMonitorIds() {
		return monitorIds;
	}
	public void setMonitorIds(String monitorIds) {
		this.monitorIds = monitorIds;
	}
}
