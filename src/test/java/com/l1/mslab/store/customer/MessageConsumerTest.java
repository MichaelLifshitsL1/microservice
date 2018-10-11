package com.l1.mslab.store.customer;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.eventbus.EventBus;
import com.l1.mslab.store.customer.domain.Customer;
import com.l1.mslab.store.customer.events.ExtrinsicEventConsumer;
import com.l1.mslab.store.customer.events.IntrinsicEventConsumer;
import com.l1.mslab.store.customer.events.IntrinsicEventProducer;
import com.l1.mslab.store.customer.events.customer.CustomerCreated;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessageConsumerTest {

	@MockBean
	IntrinsicEventConsumer intrinsicEventConsumer;

	@MockBean
	IntrinsicEventProducer intrinsicEventProducer;

	@MockBean
	ExtrinsicEventConsumer extrinsicEventProducer;

	@Inject
	EventBus eventBus;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setUp() {
		eventBus.post(new CustomerCreated(UUID.fromString("e4d284f0-2545-4368-ae80-8278c33edf16"), "John", "Doe",
				LocalDate.now()));
		eventBus.post(new CustomerCreated(UUID.fromString("e4d284f0-2545-4368-ae80-8278c33edf17"), "Jane", "Roe",
				LocalDate.now()));
	}

	@Test
	public void testOne() throws Exception {
		ResponseEntity<List<Customer>> response = restTemplate.exchange("/customer/list", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Customer>>() {
				});
		List<Customer> body = response.getBody();
		assertEquals("e4d284f0-2545-4368-ae80-8278c33edf16", body.get(0).getCustomerId().toString());
		assertEquals("e4d284f0-2545-4368-ae80-8278c33edf17", body.get(1).getCustomerId().toString());
	}

	@Test
	public void testTwo() throws Exception {
		ResponseEntity<Customer> response = restTemplate.exchange("/customer/e4d284f0-2545-4368-ae80-8278c33edf16",
				HttpMethod.GET, null, new ParameterizedTypeReference<Customer>() {
				});
		Customer body = response.getBody();
		assertEquals("e4d284f0-2545-4368-ae80-8278c33edf16", body.getCustomerId().toString());
	}
}
