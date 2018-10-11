package com.l1.mslab.store.customer.events.order;

import java.util.Date;
import java.util.UUID;

import com.l1.mslab.store.customer.events.Event;

public class OrderCreated extends Event {

	private UUID orderId;

	private UUID customerId;

	private String description;

	private Date creationDate;

	public OrderCreated() {
	}

	public OrderCreated(UUID orderId, UUID customerId, String description, Date creationDate) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.description = description;
		this.creationDate = creationDate;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public UUID getCustomerId() {
		return customerId;
	}

	public String getDescription() {
		return description;
	}

	public Date getCreationDate() {
		return creationDate;
	}
}
