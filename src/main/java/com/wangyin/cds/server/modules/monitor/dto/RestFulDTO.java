package com.wangyin.cds.server.modules.monitor.dto;
/**   
 * @author wy   
 */
public class RestFulDTO<T> {
	
	private String errorCode;
    private String errMsg;
    private T resultInfo;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public T getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(T resultInfo) {
		this.resultInfo = resultInfo;
	}
}
