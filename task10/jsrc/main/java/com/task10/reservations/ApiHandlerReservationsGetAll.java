package com.task10.reservations;

import static com.task10.util.Util.convertToJson;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
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
public class ApiHandlerReservationsGetAll implements RequestHandler<Object, APIGatewayProxyResponseEvent> {

	private static final Logger logger = Logger.getLogger(ApiHandlerReservationsGetAll.class.getName());

	public APIGatewayProxyResponseEvent handleRequest(Object request, Context context) {
		logger.info("Start ReservationsGetAll");
		logger.info("request = " + request);

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		PaginatedScanList<Reservation> result = mapper.scan(Reservation.class, scanExpression);

		Map<String, List<Reservation>> resultMap = new HashMap<>();
		resultMap.put("reservations", result);

		APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
		responseEvent.setStatusCode(200);
		responseEvent.setBody(convertToJson(resultMap));
		return responseEvent;
	}
}
