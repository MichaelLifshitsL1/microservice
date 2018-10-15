package com.l1.mslab.store.customer.events;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;

public class IntrinsicEventProducerKinesisImpl implements IntrinsicEventProducer {

	public static final String DEFAULT_REGION = "us-east-1";
	public static final String TEST_ACCESS_KEY = "test";
	public static final String TEST_SECRET_KEY = "test";
	public static final AWSCredentials TEST_CREDENTIALS = new BasicAWSCredentials(TEST_ACCESS_KEY, TEST_SECRET_KEY);

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
	//	kinesisClient.createStream("test2", 2);
		System.out.println("+++++++++++++ describeStream"+kinesisClient.describeStream("test3"));
	}

	public void publish(Event... events) {
		PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
		putRecordsRequest.setStreamName("test2");
		List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
			putRecordsRequestEntry.setData(ByteBuffer.wrap(String.valueOf(i).getBytes()));
			putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
			putRecordsRequestEntryList.add(putRecordsRequestEntry);
		}

		putRecordsRequest.setRecords(putRecordsRequestEntryList);
		PutRecordsResult putRecordsResult = kinesisClient.putRecords(putRecordsRequest);

		System.out.println("+++++++++++++ Put Result " + putRecordsResult.getRecords().size());
	}

	@PreDestroy
	public void close() {
	}

}
