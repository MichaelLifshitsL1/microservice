package com.l1.mslab.store.customer.events;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntrinsicEventProducerKinesisImpl implements IntrinsicEventProducer {

	public static final String DEFAULT_REGION = "us-east-1";
	public static final String TEST_ACCESS_KEY = "test";
	public static final String TEST_SECRET_KEY = "test";
	public static final AWSCredentials TEST_CREDENTIALS = new BasicAWSCredentials(TEST_ACCESS_KEY, TEST_SECRET_KEY);

	@Value("${kinesis.intrinsic.stream}")
	private String kinesisIntrinsicStream;

	@Inject
	Logger logger;

	private AmazonKinesis kinesisClient;

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

	public void publish(Event... events) {
		try {
			PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
			putRecordsRequest.setStreamName(kinesisIntrinsicStream);
			List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();
			for (Event event : events) {
				PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.findAndRegisterModules().writeValueAsString(event);
				logger.info("sending intrinsic event = " + json);
				putRecordsRequestEntry.setData(ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8)));
				putRecordsRequestEntry.setPartitionKey(kinesisIntrinsicStream + "-key");
				putRecordsRequestEntryList.add(putRecordsRequestEntry);
			}
			putRecordsRequest.setRecords(putRecordsRequestEntryList);
			kinesisClient.putRecords(putRecordsRequest);
		} catch (JsonProcessingException e) {
			logger.severe("Failed to send Kinesis events - " + e.getMessage());
		}
	}

	@PreDestroy
	public void close() {
	}

}
