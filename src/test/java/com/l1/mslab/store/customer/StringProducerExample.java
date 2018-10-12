package com.l1.mslab.store.customer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.ProducerFencedException;

public class StringProducerExample {

	public static void main(String args[]) {

		// properties for producer
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9192,localhost:9292");
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		try (Producer<Integer, String> producer = new KafkaProducer<Integer, String>(props)) {
			// send messages to my-topic
			for (int i = 0; i < 100; i++) {
				ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>("my-topic", i,
						"Test Message #" + Integer.toString(i));
				RecordMetadata metadata = producer.send(producerRecord).get();
				System.out.println("published event metadata = " + metadata);
			}
		} catch (ProducerFencedException | InterruptedException | ExecutionException e) {
			throw new Error (e);
		}

	}

}
