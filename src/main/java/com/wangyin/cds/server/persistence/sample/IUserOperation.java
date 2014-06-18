package com.wangyin.cds.server.persistence.sample;

import com.wangyin.cds.server.persistence.sample.model.User;

/**   
 * @author wy   
 */
public interface IUserOperation {

	 public User selectUserByID(int id);
}
