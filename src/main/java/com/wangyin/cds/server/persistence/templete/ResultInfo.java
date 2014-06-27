package com.wangyin.cds.server.persistence.templete;
/**   
 * @author wy   
 */
public class ResultInfo {
	
	private boolean isSuccess;
	private String message;
	private Object result;
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}

}
