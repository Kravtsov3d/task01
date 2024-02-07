package com.task10.tables;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.tables.model.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@LambdaHandler(
	lambdaName = "api_handler_tables_get_all",
	roleName = "api_handler_tables_get_all-role"
)
public class ApiHandlerTablesGetAll implements RequestHandler<Object, Map<String, List<Table>>> {

	private static final Logger logger = Logger.getLogger(ApiHandlerTablesGetAll.class.getName());

	public Map<String, List<Table>> handleRequest(Object request, Context context) {
		logger.info("Start TablesGetAll");
		logger.info("request = " + request);

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		PaginatedScanList<Table> result = mapper.scan(Table.class, scanExpression);

		Map<String, List<Table>> resultMap = new HashMap<>();
		resultMap.put("tables", result);
		return resultMap;
	}


}
