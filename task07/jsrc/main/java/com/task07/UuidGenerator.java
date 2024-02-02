package com.task07;

import static java.util.stream.Collectors.toList;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syndicate.deployment.annotations.events.RuleEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import java.io.ByteArrayInputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@LambdaHandler(
	lambdaName = "uuid_generator",
	roleName = "uuid_generator-role"
)
@RuleEventSource(targetRule = "uuid_trigger")
public class UuidGenerator implements RequestHandler<ScheduledEvent, Void> {

	private static final Logger logger = Logger.getLogger(UuidGenerator.class.getName());

	public Void handleRequest(ScheduledEvent event, Context context) {
		logger.info("Init S3 client");
		AmazonS3 s3Client = AmazonS3Client.builder().build();

		final byte[] file = createFile();
		final String fileName = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(file);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.length);
		metadata.setContentType("json");

		logger.info("Put file " + fileName + " to S3");
		s3Client.putObject("cmtr-6e999703-uuid-storage-test", fileName, inputStream, metadata);

		return null;
	}

	private static byte[] createFile() {
		logger.info("Create file content");

		final List<String> ids = IntStream.range(0, 10)
			.mapToObj(i -> UUID.randomUUID().toString())
			.collect(toList());

		final HashMap<String, List<String>> object = new HashMap<>();
		object.put("ids", ids);

		try {
			return new ObjectMapper().writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}
}
