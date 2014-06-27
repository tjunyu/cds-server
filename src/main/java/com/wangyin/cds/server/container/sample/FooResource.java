package com.wangyin.cds.server.container.sample;

import java.util.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wangyin.cds.server.session.ICdsSession;
@Path("foo")
public class FooResource {
	@Context
	ICdsSession session;
	
	@GET
	@Path("bar")
	@Produces(MediaType.APPLICATION_JSON)
	public Foo getFoo(){
		Foo foo = new Foo();
		foo.setName("David Chan");
		foo.setDate(Calendar.getInstance().getTime());
		return foo;
	}
}
