package com.task06;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.syndicate.deployment.annotations.events.DynamoDbTriggerEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@LambdaHandler(
	lambdaName = "audit_producer",
	roleName = "audit_producer-role"
)
@DynamoDbTriggerEventSource(targetTable = "Configuration", batchSize = 1)
public class AuditProducer implements RequestHandler<DynamodbEvent, Void> {

	private static final Logger logger = Logger.getLogger(AuditProducer.class.getName());
	private static final String AUDIT_TABLE = "Audit"; //cmtr-6e999703-Audit-test

	private AmazonDynamoDB amazonDynamoDB;

	public Void handleRequest(DynamodbEvent event, Context context) {
		logger.info("Init DynamoDB client");
		amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.EU_CENTRAL_1)
			.build();

		logger.info("Start process records");
		event.getRecords().forEach(this::processRecord);
		logger.info("End process records");

		return null;
	}

	private void processRecord(final DynamodbEvent.DynamodbStreamRecord record) {
		switch (record.getEventName()) {
			case "INSERT":
				saveInsertedConfiguration(record.getDynamodb().getNewImage());
				break;
			case "MODIFY":
				saveModifiedConfiguration(
					record.getDynamodb().getNewImage(),
					record.getDynamodb().getOldImage()
				);
				break;
			default:
				throw new IllegalArgumentException("Event name not supported");
		}
	}

	private void saveInsertedConfiguration(
		Map<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue> newImage
	) throws ConditionalCheckFailedException {
		final String id = String.valueOf(UUID.randomUUID());
		final String modificationTime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
		final AttributeValue key = convertAttribute(newImage, "key");

		Map<String, AttributeValue> newValue = new HashMap<>();
		newValue.put("key", new AttributeValue().withS("HELLO"));
		newValue.put("value", new AttributeValue().withS("3000"));

		Map<String, AttributeValue> attributes = new HashMap<>();
		attributes.put("id", new AttributeValue().withS(id));
		attributes.put("itemKey", key);
		attributes.put("modificationTime", new AttributeValue().withS(modificationTime));
		attributes.put("newValue", new AttributeValue().withM(newValue));

		logger.info("Save inserted configuration to audit");
		amazonDynamoDB.putItem(AUDIT_TABLE, attributes);
	}

	private void saveModifiedConfiguration(
		Map<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue> newImage,
		Map<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue> oldImage
	) throws ConditionalCheckFailedException {
		final String id = String.valueOf(UUID.randomUUID());
		final String modificationTime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
		final AttributeValue key = convertAttribute(newImage, "key");

		Map<String, AttributeValue> attributes = new HashMap<>();
		attributes.put("id", new AttributeValue().withS(id));
		attributes.put("itemKey", key);
		attributes.put("modificationTime", new AttributeValue().withS(modificationTime));
		attributes.put("oldValue", convertAttribute(oldImage, "value"));
		attributes.put("newValue", convertAttribute(newImage, "value"));

		logger.info("Save modified configuration to audit");
		amazonDynamoDB.putItem(AUDIT_TABLE, attributes);
	}

	private static AttributeValue convertAttribute(
		final Map<String, com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue> newImage,
		final String key
	) {
		return new AttributeValue().withS(newImage.get(key).getS());
	}
}
