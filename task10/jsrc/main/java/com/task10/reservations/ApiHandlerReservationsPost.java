package com.task10.reservations;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.reservations.model.Reservation;
import java.util.logging.Logger;

@LambdaHandler(
	lambdaName = "api_handler_reservations_post",
	roleName = "api_handler_reservations_post-role"
)
public class ApiHandlerReservationsPost implements RequestHandler<Reservation, Void> {

	private static final Logger logger = Logger.getLogger(ApiHandlerReservationsPost.class.getName());

	public Void handleRequest(Reservation request, Context context) {
		logger.info("Start ReservationsPost");
		logger.info("request = " + request);

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
		DynamoDBMapper mapper = new DynamoDBMapper(client);

		mapper.save(request);
		return null;
	}
}
