package com.l1.mslab.store.customer;

import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.google.common.eventbus.EventBus;
import com.l1.mslab.store.customer.events.ExtrinsicEventConsumer;
import com.l1.mslab.store.customer.events.ExtrinsicEventConsumerKafkaImpl;
import com.l1.mslab.store.customer.events.ExtrinsicEventConsumerKinesisImpl;
import com.l1.mslab.store.customer.events.IntrinsicEventConsumer;
import com.l1.mslab.store.customer.events.IntrinsicEventConsumerKafkaImpl;
import com.l1.mslab.store.customer.events.IntrinsicEventConsumerKinesisImpl;
import com.l1.mslab.store.customer.events.IntrinsicEventProducer;
import com.l1.mslab.store.customer.events.IntrinsicEventProducerKafkaImpl;
import com.l1.mslab.store.customer.events.IntrinsicEventProducerKinesisImpl;

@Configuration
public class SpringConfig {

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;

	@Value("${mslab.store.messaging}")
	private StoreMessagingType storeMessaging;

	@Bean(name = "producerProperties")
	public Properties producerProperties() {
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", kafkaBootstrapServers);
		return props;
	}

	@Bean(name = "consumerProperties")
	public Properties consumerProperties() {
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", kafkaBootstrapServers);
		// for a new consumer group.id to read the whole topic from beginning
		props.setProperty("auto.offset.reset", "earliest");
		return props;
	}

	@Bean
	public ExtrinsicEventConsumer extrinsicEventConsumer() {
		switch (storeMessaging) {
		case kinesis:
			return new ExtrinsicEventConsumerKinesisImpl();
		case kafka:
			return new ExtrinsicEventConsumerKafkaImpl();
		default:
			throw new Error("mslab.store.messaging type can't be found");
		}
	}

	@Bean
	public IntrinsicEventConsumer intrinsicEventConsumer() {
		switch (storeMessaging) {
		case kinesis:
			return new IntrinsicEventConsumerKinesisImpl();
		case kafka:
			return new IntrinsicEventConsumerKafkaImpl();
		default:
			throw new Error("mslab.store.messaging type can't be found");
		}
	}

	@Bean
	public IntrinsicEventProducer intrinsicEventProducer() {
		switch (storeMessaging) {
		case kinesis:
			return new IntrinsicEventProducerKinesisImpl();
		case kafka:
			return new IntrinsicEventProducerKafkaImpl();
		default:
			throw new Error("mslab.store.messaging type can't be found");
		}
	}

	@Bean
	public Logger exposeLogger(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

	@Bean
	public EventBus eventBus() {
		return new EventBus();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Bean
	public CommandLineRunner schedulingExtrinsicEventConsumer(TaskExecutor executor,
			ExtrinsicEventConsumer extrinsicEventConsumer) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				executor.execute(extrinsicEventConsumer);
			}
		};
	}

	@Bean
	public CommandLineRunner schedulingIntrinsicEventConsumer(TaskExecutor executor,
			IntrinsicEventConsumer intrinsicEventConsumer) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				executor.execute(intrinsicEventConsumer);
			}
		};
	}
}
