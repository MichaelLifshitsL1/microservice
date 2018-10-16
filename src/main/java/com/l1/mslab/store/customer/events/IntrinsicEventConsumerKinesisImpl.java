package com.l1.mslab.store.customer.events;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorResult;
import com.amazonaws.services.kinesis.model.Record;
import com.amazonaws.services.kinesis.model.ShardIteratorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;

public class IntrinsicEventConsumerKinesisImpl implements IntrinsicEventConsumer {

	public static final String DEFAULT_REGION = "us-east-1";
	public static final String TEST_ACCESS_KEY = "test";
	public static final String TEST_SECRET_KEY = "test";
	public static final AWSCredentials TEST_CREDENTIALS = new BasicAWSCredentials(TEST_ACCESS_KEY, TEST_SECRET_KEY);

	@Value("${kinesis.intrinsic.stream}")
	private String kinesisIntrinsicStream;

	private AmazonKinesis kinesisClient;

	@Inject
	EventBus eventBus;

	@Inject
	Logger logger;

	@PostConstruct
	private void init() {
		System.setProperty("com.amazonaws.sdk.disableCbor", "true");
		kinesisClient = AmazonKinesisClientBuilder.standard()
				.withEndpointConfiguration(
						new AwsClientBuilder.EndpointConfiguration("http://localhost:4568/", DEFAULT_REGION))
				.withCredentials(new AWSStaticCredentialsProvider(TEST_CREDENTIALS)).build();
		try {
			kinesisClient.createStream(kinesisIntrinsicStream, 2);
		} catch (Exception e) {
			logger.warning("Stream " + kinesisIntrinsicStream + " exist");
		}
	}

	@Override
	public void run() {
		GetShardIteratorRequest request = new GetShardIteratorRequest();
		request.setStreamName(kinesisIntrinsicStream);
		request.setShardId("shardId-000000000001");
		request.setShardIteratorType(ShardIteratorType.LATEST);
		GetShardIteratorResult iterator = kinesisClient.getShardIterator(request);
		String token = iterator.getShardIterator();
		while (true) {
			GetRecordsRequest req = new GetRecordsRequest();
			req.setShardIterator(token);
			req.setLimit(1024);
			GetRecordsResult records = kinesisClient.getRecords(req);
			token = records.getNextShardIterator();
			for (Record record : records.getRecords()) {
				String json = StandardCharsets.UTF_8.decode(record.getData()).toString();
				logger.info("receiving intrinsic event = " + json);
				ObjectMapper mapper = new ObjectMapper();
				try {
					Event event = mapper.findAndRegisterModules().readValue(json, Event.class);
					eventBus.post(event);
				} catch (IOException e) {
					throw new Error(e);
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}
	}

	@PreDestroy
	public void close() {
	}

}
