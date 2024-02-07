package com.task10.reservations;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.reservations.model.Reservation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@LambdaHandler(
	lambdaName = "api_handler_reservations_get_all",
	roleName = "api_handler_reservations_get_all-role"
)
public class ApiHandlerReservationsGetAll implements RequestHandler<Object, Map<String, List<Reservation>>> {

	private static final Logger logger = Logger.getLogger(ApiHandlerReservationsGetAll.class.getName());

	public Map<String, List<Reservation>> handleRequest(Object request, Context context) {
		AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
		DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		PaginatedScanList<Reservation> scanResult = mapper.scan(Reservation.class, scanExpression);
		final ItemCollection<ScanOutcome> reservations = dynamoDB.getTable("Reservations").scan(scanSpec);//cmtr-6e999703-Reservations-test

		Map<String, List<Reservation>> resultMap = new HashMap<>();
		resultMap.put("reservations", null);
		return resultMap;
	}
}
