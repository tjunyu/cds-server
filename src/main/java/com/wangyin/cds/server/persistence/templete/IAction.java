package com.wangyin.cds.server.persistence.templete;

import org.apache.ibatis.session.SqlSession;

/**   
 * @author wy   
 */
public interface IAction {
	
	public Object doAction(SqlSession session) throws Exception;

}
