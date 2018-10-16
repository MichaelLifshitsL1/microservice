package com.l1.mslab.store.customer.events.customer;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerCreated extends CustomerInfoEvent {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
