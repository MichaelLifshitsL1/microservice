package com.l1.mslab.store.customer.events.order;

import java.util.UUID;

import com.l1.mslab.store.customer.events.Event;

public class OrderStatusModifyed extends Event {

	private UUID orderId;

	private OrderStatus newOrderStatus;

	private OrderStatus oldOrderStatus;

	public OrderStatusModifyed() {
	}

	public OrderStatusModifyed(UUID orderId, OrderStatus newOrderStatus, OrderStatus oldOrderStatus) {
		this.orderId = orderId;
		this.newOrderStatus = newOrderStatus;
		this.oldOrderStatus = oldOrderStatus;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public OrderStatus getNewOrderStatus() {
		return newOrderStatus;
	}

	public OrderStatus getOldOrderStatus() {
		return oldOrderStatus;
	}
}
