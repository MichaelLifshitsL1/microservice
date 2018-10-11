package com.l1.mslab.store.customer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.l1.mslab.store.customer.events.Event;
import com.l1.mslab.store.customer.events.ExtrinsicEventConsumer;
import com.l1.mslab.store.customer.events.IntrinsicEventConsumer;
import com.l1.mslab.store.customer.events.IntrinsicEventProducer;
import com.l1.mslab.store.customer.events.customer.CustomerCreated;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessageProducerTest {

	@MockBean
	IntrinsicEventConsumer intrinsicEventConsumer;

	@MockBean
	IntrinsicEventProducer intrinsicEventProducer;

	@MockBean
	ExtrinsicEventConsumer extrinsicEventProducer;

	@Captor
	ArgumentCaptor<Event> eventCaptor;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testOne() throws Exception {
		restTemplate.postForEntity("/customer?firstName=Bob&lastName=Poe", null, Void.class);

		Mockito.verify(intrinsicEventProducer).publish(eventCaptor.capture());
		assertEquals("Bob", ((CustomerCreated) eventCaptor.getValue()).getFirstName());
	}
}
