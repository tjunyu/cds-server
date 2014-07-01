package com.wangyin.cds.server.persistence.model;

import java.io.Serializable;
import java.util.Date;

/**
 * wy
 */
public class DbAlarmInstance implements Serializable {
    private Integer id;
    private Integer dbAlarmId;
    private Integer dbInfoId;
    private Integer dbMinitorInstanceId;
    private String alarmMsg;
    private Date creationDate;
    private String alarmStatus;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDbAlarmId() {
        return this.dbAlarmId;
    }

    public void setDbAlarmId(Integer dbAlarmId) {
        this.dbAlarmId = dbAlarmId;
    }

    public Integer getDbInfoId() {
        return this.dbInfoId;
    }

    public void setDbInfoId(Integer dbInfoId) {
        this.dbInfoId = dbInfoId;
    }

    public Integer getDbMinitorInstanceId() {
        return this.dbMinitorInstanceId;
    }

    public void setDbMinitorInstanceId(Integer dbMinitorInstanceId) {
        this.dbMinitorInstanceId = dbMinitorInstanceId;
    }

    public String getAlarmMsg() {
        return this.alarmMsg;
    }

    public void setAlarmMsg(String alarmMsg) {
        this.alarmMsg = alarmMsg;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAlarmStatus() {
        return this.alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }
}
