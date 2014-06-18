package com.wangyin.cds.server.modules.monitor.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.ibatis.session.SqlSession;

import com.wangyin.cds.server.persistence.PersistenceManager;
import com.wangyin.cds.server.persistence.sample.IUserOperation;
import com.wangyin.cds.server.persistence.sample.model.User;

/**
 * @author wy
 */
@Path("fooService")
public class FooService {

	@GET
	@Path("user")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser() {
		SqlSession session = PersistenceManager.getSession().openSession();
		User user = null;
		try {
			IUserOperation userOperation = session
					.getMapper(IUserOperation.class);
			user = userOperation.selectUserByID(1);
			System.out.println(user.getUserAddress());
			System.out.println(user.getUserName());
		} finally {
			session.close();
		}
		return user;
	}

}
