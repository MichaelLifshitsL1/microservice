package com.l1.mslab.store.customer.events.customer;

import java.time.LocalDate;
import java.util.UUID;

public class CustomerCreated extends CustomerInfoEvent {

	private LocalDate fromDate;

	public CustomerCreated() {
	}

	public CustomerCreated(UUID customerId, String firstName, String lastName, LocalDate fromDate) {
		super(customerId, firstName, lastName);
		this.fromDate = fromDate;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}
}
