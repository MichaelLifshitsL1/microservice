package com.l1.mslab.store.customer.rest;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.l1.mslab.store.customer.domain.Customer;
import com.l1.mslab.store.customer.service.CustomerCommandService;
import com.l1.mslab.store.customer.service.CustomerQueryService;

@Component
@Path("/customer")
public class CustomerResource {

	@Inject
	private CustomerQueryService queryService;

	@Inject
	private CustomerCommandService commandService;

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Customer> getAllCustomers() {
		return queryService.getAllCustomers();
	}

	@GET
	@Path("/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@PathParam("customerId") @NotNull UUID customerId) {
		return queryService.getCustomer(customerId);
	}

	@POST
	public void createCustomer(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
		commandService.createCustomer(firstName, lastName);
	}

	@PUT
	@Path("/{customerId}")
	public void modifyCustomer(@PathParam("customerId") @NotNull UUID customerId,
			@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
		commandService.modifyCustomer(customerId, firstName, lastName);
	}

}
