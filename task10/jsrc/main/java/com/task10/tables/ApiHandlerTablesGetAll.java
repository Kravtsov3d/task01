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
	lambdaName = "api_handler_tables_get_all",
	roleName = "api_handler_tables_get_all-role"
)
public class ApiHandlerTablesGetAll implements RequestHandler<Object, Map<String, Object>> {

	private static final Logger logger = Logger.getLogger(ApiHandlerTablesGetAll.class.getName());
	private AmazonDynamoDB amazonDynamoDB;

	public Map<String, Object> handleRequest(Object request, Context context) {
		logger.info("Start TablesGetAll");
		amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.EU_CENTRAL_1)
			.build();



		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("statusCode", 200);
		resultMap.put("body", "Hello from Lambda");
		return resultMap;
	}


}
