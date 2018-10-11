package com.l1.mslab.store.customer.service;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.l1.mslab.store.customer.domain.Customer;
import com.l1.mslab.store.customer.events.customer.CustomerCreated;
import com.l1.mslab.store.customer.events.customer.CustomerInfoModifyed;
import com.l1.mslab.store.customer.repo.CustomerRepository;

@Named
public class CustomerEventListener {

	@Inject
	Logger logger;

	@Inject
	EventBus eventBus;

	@Inject
	CustomerRepository customerRepository;

	@PostConstruct
	public void init() {
		eventBus.register(this);
	}

	@Subscribe
	public void apply(CustomerCreated event) {
		customerRepository.save(
				new Customer(event.getCustomerId(), event.getFirstName(), event.getLastName(), event.getFromDate()));
	}

	@Subscribe
	public void apply(CustomerInfoModifyed event) {
		Customer customer = customerRepository.findById(event.getCustomerId()).get();
		customer.setFirstName(event.getFirstName());
		customer.setLastName(event.getLastName());
		customerRepository.save(customer);
	}

}
