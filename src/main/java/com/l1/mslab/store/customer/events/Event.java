package com.l1.mslab.store.customer.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.l1.mslab.store.customer.events.customer.CustomerCreated;
import com.l1.mslab.store.customer.events.customer.CustomerInfoModifyed;
import com.l1.mslab.store.customer.events.order.OrderCreated;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = CustomerCreated.class, name = "CustomerCreated"),
		@JsonSubTypes.Type(value = CustomerInfoModifyed.class, name = "CustomerInfoModifyed"),
		@JsonSubTypes.Type(value = OrderCreated.class, name = "OrderCreated") })
public abstract class Event {

}
