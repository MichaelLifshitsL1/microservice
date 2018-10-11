package com.l1.mslab.store.customer.service;

import java.time.LocalDate;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import com.l1.mslab.store.customer.events.IntrinsicEventProducer;
import com.l1.mslab.store.customer.events.customer.CustomerCreated;
import com.l1.mslab.store.customer.events.customer.CustomerInfoModifyed;

@Named
public class CustomerCommandService {

	@Inject
	IntrinsicEventProducer eventProducer;

	public void createCustomer(String firstName, String lastName) {
		eventProducer.publish(new CustomerCreated(UUID.randomUUID(), firstName, lastName, LocalDate.now()));

	}

	public void modifyCustomer(UUID customerId, String firstName, String lastName) {
		eventProducer.publish(new CustomerInfoModifyed(customerId, firstName, lastName));
	}

}
