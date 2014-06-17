package com.wangyin.cds.server.container.sample;

import java.util.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
@Path("foo")
public class FooResource {
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
