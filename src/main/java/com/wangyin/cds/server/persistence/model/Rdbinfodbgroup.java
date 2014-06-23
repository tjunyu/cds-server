package com.wangyin.cds.server.persistence.model;
import java.io.Serializable;

/**   
 * @author wy   
 */
public class Rdbinfodbgroup implements Serializable {
	private Integer rDbInfoDbGroupId;
	private Integer dbGroupId;
	private Integer dbInfoId;
	private String masterOrSlave;

	public Integer getRDbInfoDbGroupId() {
 		return this.rDbInfoDbGroupId;
	}
	
	public void setRDbInfoDbGroupId(Integer rDbInfoDbGroupId) {
 		this.rDbInfoDbGroupId = rDbInfoDbGroupId;
	}

	public Integer getDbGroupId() {
 		return this.dbGroupId;
	}
	
	public void setDbGroupId(Integer dbGroupId) {
 		this.dbGroupId = dbGroupId;
	}

	public Integer getDbInfoId() {
 		return this.dbInfoId;
	}
	
	public void setDbInfoId(Integer dbInfoId) {
 		this.dbInfoId = dbInfoId;
	}

	public String getMasterOrSlave() {
 		return this.masterOrSlave;
	}
	
	public void setMasterOrSlave(String masterOrSlave) {
 		this.masterOrSlave = masterOrSlave;
	}
}
