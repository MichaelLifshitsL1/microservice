package com.l1.mslab.store.customer.events;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class EventDeserializer implements Deserializer<Event> {

	private static final Logger logger = Logger.getLogger(EventDeserializer.class.getName());

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
	}

	@Override
	public Event deserialize(final String topic, final byte[] data) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.readValue(data, Event.class);
		} catch (IOException e) {
			logger.severe("Could not deserialize event: " + e.getMessage());
			throw new SerializationException("Could not deserialize event", e);
		}
	}

	@Override
	public void close() {
	}

}
