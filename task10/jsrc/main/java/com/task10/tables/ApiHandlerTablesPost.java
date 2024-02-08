package com.task10.tables;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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
public class ApiHandlerTablesPost implements RequestHandler<Table, Map<String, Integer>> {

	private static final Logger logger = Logger.getLogger(ApiHandlerTablesPost.class.getName());

	public Map<String, Integer> handleRequest(Table request, Context context) {
		logger.info("Start TablesGetById");
		logger.info("request = " + request);

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		logger.info("Start save");
		mapper.save(request);

		logger.info("Prepare response");
		final Map<String, Integer> result = new HashMap<>();
		result.put("id", request.getId());
		return result;
	}
}
