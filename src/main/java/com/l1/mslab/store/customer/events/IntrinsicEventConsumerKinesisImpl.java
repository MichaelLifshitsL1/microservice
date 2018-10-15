package com.l1.mslab.store.customer.events;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import com.google.common.eventbus.EventBus;

public class IntrinsicEventConsumerKinesisImpl implements IntrinsicEventConsumer {

	@Inject
	EventBus eventBus;

	@Inject
	Logger logger;

	@PostConstruct
	private void init() {
	}

	@Override
	public void run() {
	}

	@PreDestroy
	public void close() {
	}

}
