package com.l1.mslab.store.customer.events;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

@Named
public class IntrinsicEventProducer {

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

	public void publish(Event event) {
		// "...-key" should prescribe Kafka to append all the messages to the same
		// partition, guaranteeing fixed order. This is good for mutable entities only.
		// This approach will guaranty same state of the db on replaying sourced events.
		// Immutable entities should be distributed across all the partitions.
		final ProducerRecord<String, Event> record = new ProducerRecord<>(kafkaIntrinsicTopic,
				kafkaIntrinsicTopic + "-key", event);
		try {
			producer.beginTransaction();
			logger.info("publishing event = " + record);
			RecordMetadata metadata = producer.send(record).get();
			logger.info("published event metadata = " + metadata);
			producer.commitTransaction();
		} catch (ProducerFencedException | InterruptedException | ExecutionException e) {
			producer.close();
			logger.warning(e.getMessage());
		} catch (KafkaException e) {
			producer.abortTransaction();
			logger.warning("Failed to send Kafka event - " + e.getMessage());
		}
	}

	@PreDestroy
	public void close() {
		producer.close();
	}

}
