package com.wangyin.cds.server.persistence.model;

import java.io.Serializable;
import java.util.Date;

/**
 * wy
 */
public class DbAlarm implements Serializable {
    private Integer id;
    private Integer dbMonitorGroupId;
    private Integer userGroupId;
    private String alarmType;
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

    public Integer getDbMonitorGroupId() {
        return this.dbMonitorGroupId;
    }

    public void setDbMonitorGroupId(Integer dbMonitorGroupId) {
        this.dbMonitorGroupId = dbMonitorGroupId;
    }

    public Integer getUserGroupId() {
        return this.userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getAlarmType() {
        return this.alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
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
