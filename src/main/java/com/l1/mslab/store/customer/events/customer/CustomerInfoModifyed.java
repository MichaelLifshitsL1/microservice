package com.l1.mslab.store.customer.events.customer;

import java.util.UUID;

public class CustomerInfoModifyed extends CustomerInfoEvent {

	public CustomerInfoModifyed() {
	}

	public CustomerInfoModifyed(UUID customerId, String firstName, String lastName) {
		super(customerId, firstName, lastName);
	}

}
