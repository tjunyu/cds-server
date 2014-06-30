package com.wangyin.cds.server.persistence.model;

import java.util.Date;
import java.io.Serializable;

/**   
 * @author wy   
 */
public class DbEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9211540384274120587L;
    private Integer id;
    private Integer eventId;
    private String eventType;
    private Integer dbMonitorGroupId;
    private Integer dbInfoId;
    private Integer dbMonitorId;
    private String ip;
    private Integer port;
    private String dbType;
    private String createBy;
    private Date creationDate;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventId() {
        return this.eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return this.eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getDbMonitorGroupId() {
        return this.dbMonitorGroupId;
    }

    public void setDbMonitorGroupId(Integer dbMonitorGroupId) {
        this.dbMonitorGroupId = dbMonitorGroupId;
    }

    public Integer getDbInfoId() {
        return this.dbInfoId;
    }

    public void setDbInfoId(Integer dbInfoId) {
        this.dbInfoId = dbInfoId;
    }

    public Integer getDbMonitorId() {
        return this.dbMonitorId;
    }

    public void setDbMonitorId(Integer dbMonitorId) {
        this.dbMonitorId = dbMonitorId;
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

    public String getDbType() {
        return this.dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
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
}
