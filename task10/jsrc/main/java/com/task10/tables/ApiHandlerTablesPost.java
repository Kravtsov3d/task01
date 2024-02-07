package com.task10.tables;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.tables.model.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@LambdaHandler(
	lambdaName = "api_handler_tables_post",
	roleName = "api_handler_tables_post-role"
)
public class ApiHandlerTablesPost implements RequestHandler<Table, Void> {

	private static final Logger logger = Logger.getLogger(ApiHandlerTablesPost.class.getName());
	private AmazonDynamoDB amazonDynamoDB;

	public Void handleRequest(Table request, Context context) {
		logger.info("Start TablesGetById");
		amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.EU_CENTRAL_1)
			.build();

		persistTable(request);

		return null;
	}

	private void persistTable(Table table) throws ConditionalCheckFailedException {
		Map<String, AttributeValue> attributesMap = new HashMap<>();
		attributesMap.put("id", new AttributeValue().withN(String.valueOf(table.getId())));
		attributesMap.put("number", new AttributeValue().withN(String.valueOf(table.getNumber())));
		attributesMap.put("places", new AttributeValue().withN(String.valueOf(table.getPlaces())));
		attributesMap.put("isVip", new AttributeValue().withBOOL(table.isVip()));
		attributesMap.put("minOrder", new AttributeValue().withN(String.valueOf(table.getMinOrder())));

		logger.info("Persist Tables");
		amazonDynamoDB.putItem("Tables", attributesMap);//cmtr-6e999703-Tables-test
	}
}
