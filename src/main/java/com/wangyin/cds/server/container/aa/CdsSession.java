package com.wangyin.cds.server.container.aa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 服务器session
 * @author david
 *
 */
public class CdsSession implements Serializable, ICdsSession {

	private static final long serialVersionUID = -7167951196806744589L;
	private String id;
	private Map<String, Serializable> attributes = new HashMap<String, Serializable>();
	
	private final long createdTime;
	private long lastAccessTime;
	private int status = SessionManager.STATUS_NORMAL;
	public CdsSession(String id) {
		super();
		this.id = id;
		this.createdTime = System.currentTimeMillis();
		this.lastAccessTime = this.createdTime;
	}
	/* (non-Javadoc)
	 * @see com.wangyin.cds.server.container.session.ICdsSession#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see com.wangyin.cds.server.container.session.ICdsSession#getId()
	 */
	public String getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see com.wangyin.cds.server.container.session.ICdsSession#setAttribute(java.lang.String, java.io.Serializable)
	 */
	public void setAttribute(String name, Serializable object){
		attributes.put(name, object);
	}
	//close and remove from server
	/* (non-Javadoc)
	 * @see com.wangyin.cds.server.container.session.ICdsSession#close()
	 */
	public void close(){
		
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return status;
	}
	public void touch(){
		this.lastAccessTime = System.currentTimeMillis();
		setStatus(SessionManager.STATUS_NEED_UPDATE);
	}
	public boolean exceedDuration(long period){
		return System.currentTimeMillis() - this.lastAccessTime > period;
	}
	public void removeAttribute(String name) {
		attributes.remove(name);
	}
	public long getCreatedTime() {
		return createdTime;
	}
	public Serializable getAttribute(String name) {
		return attributes.get(name);
	}
}
