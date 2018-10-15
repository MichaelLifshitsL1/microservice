package com.l1.mslab.store.customer.events;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.eventbus.EventBus;

public class ExtrinsicEventConsumerKafkaImpl implements ExtrinsicEventConsumer {

	@Value("${kafka.extrinsic.topics}")
	private String kafkaExtrinsicTopics;

	@Value("${kafka.intrinsic.topic}")
	private String kafkaIntrinsicTopic;

	private Consumer<String, Event> consumer;

	@Inject
	@Qualifier("consumerProperties")
	Properties kafkaProperties;

	@Inject
	EventBus eventBus;

	@Inject
	Logger logger;

	@PostConstruct
	private void init() {
		kafkaProperties.put("group.id", kafkaIntrinsicTopic + "-handler");
		consumer = new KafkaConsumer<>(kafkaProperties, new StringDeserializer(), new EventDeserializer());
	}

	@Override
	public void run() {
		try {
			consumer.subscribe(Arrays.asList(kafkaExtrinsicTopics.split(",")));
			while (true) {
				ConsumerRecords<String, Event> records = consumer.poll(1000);
				for (ConsumerRecord<String, Event> record : records) {
					logger.info("receiving extrinsic event = " + record);
					eventBus.post(record.value());
				}
			}
		} catch (WakeupException e) {
			// ignore for shutdown
		} finally {
			consumer.close();
		}

	}

	@PreDestroy
	public void close() {
		consumer.wakeup();
	}

}
