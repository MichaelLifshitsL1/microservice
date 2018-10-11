package com.l1.mslab.store.customer.events;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class EventSerializer implements Serializer<Event> {

	private static final Logger logger = Logger.getLogger(EventSerializer.class.getName());

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey) {
	}

	@Override
	public byte[] serialize(final String topic, final Event event) {
		try {
			if (event == null) {
				return null;
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.writeValueAsString(event).getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.severe("Could not serialize event: " + e.getMessage());
			throw new SerializationException("Could not serialize event", e);
		}
	}

	@Override
	public void close() {
	}

}
