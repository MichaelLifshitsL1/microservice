package com.l1.mslab.store.customer.events;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

public class IntrinsicEventProducerKafkaImpl implements IntrinsicEventProducer {

	@Value("${kafka.intrinsic.topic}")
	private String kafkaIntrinsicTopic;

	private Producer<String, Event> producer;

	@Inject
	@Qualifier("producerProperties")
	Properties kafkaProperties;

	@Inject
	Logger logger;

	@PostConstruct
	private void init() {
		kafkaProperties.put("transactional.id", UUID.randomUUID().toString());
		producer = new KafkaProducer<>(kafkaProperties, new StringSerializer(), new EventSerializer());
		producer.initTransactions();
	}

	public void publish(Event... events) {
		// "...-key" should prescribe Kafka to append all the messages to the same
		// partition, guaranteeing fixed order. This is good for mutable entities only.
		// This approach will guaranty same state of the db on replaying sourced events.
		// Immutable entities should be distributed across all the partitions.
		try {
			producer.beginTransaction();
			send(events);
			producer.commitTransaction();
		} catch (ProducerFencedException | InterruptedException | ExecutionException e) {
			producer.close();
			logger.warning(e.getMessage());
		} catch (KafkaException e) {
			producer.abortTransaction();
			logger.warning("Failed to send Kafka event - " + e.getMessage());
		}
	}

	private void send(Event... events) throws InterruptedException, ExecutionException {
		for (final Event event : events) {
			final ProducerRecord<String, Event> record = new ProducerRecord<>(kafkaIntrinsicTopic,
					kafkaIntrinsicTopic + "-key", event);
			logger.info("publishing event = " + record);
			RecordMetadata metadata = producer.send(record).get();
			logger.info("published event metadata = " + metadata);
		}
	}

	@PreDestroy
	public void close() {
		producer.close();
	}

}
