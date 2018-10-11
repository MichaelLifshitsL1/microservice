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
import com.l1.mslab.store.customer.events.IntrinsicEventConsumer;

@Configuration
public class SpringConfig {

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;

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
