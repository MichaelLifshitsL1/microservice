package com.l1.mslab.store.customer.events;

import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.eventbus.EventBus;

public class ExtrinsicEventConsumerKinesisImpl implements ExtrinsicEventConsumer {

	@Inject
	@Qualifier("consumerProperties")
	Properties kafkaProperties;

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
