package com.wangyin.cds.server.container;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	public ObjectMapper getContext(Class<?> type) {
		//TODO check type??
		return createObjectMapper();
	}

	private ObjectMapper createObjectMapper() {
		 ObjectMapper om = new ObjectMapper();
		 om.configure(Feature.INDENT_OUTPUT, true);
		 return om;
	}

}
