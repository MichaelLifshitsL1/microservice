package com.l1.mslab.store.customer;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.l1.mslab.store.customer.rest.CustomerResource;

@Configuration
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(CustomerResource.class);
	}

}
