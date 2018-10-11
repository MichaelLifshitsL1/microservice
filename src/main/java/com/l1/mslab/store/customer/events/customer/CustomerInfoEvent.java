package com.l1.mslab.store.customer.events.customer;

import java.util.UUID;

import com.l1.mslab.store.customer.events.Event;

public abstract class CustomerInfoEvent extends Event {

	private UUID customerId;

	private String firstName;

	private String lastName;

	public CustomerInfoEvent() {
	}

	public CustomerInfoEvent(UUID customerId, String firstName, String lastName) {
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
