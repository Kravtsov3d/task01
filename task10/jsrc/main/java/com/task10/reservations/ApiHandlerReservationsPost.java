package com.task10.reservations;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.reservations.model.Reservation;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@LambdaHandler(
	lambdaName = "api_handler_reservations_post",
	roleName = "api_handler_reservations_post-role"
)
public class ApiHandlerReservationsPost implements RequestHandler<Reservation, Void> {

	private static final Logger logger = Logger.getLogger(ApiHandlerReservationsPost.class.getName());
	private AmazonDynamoDB amazonDynamoDB;

	public Void handleRequest(Reservation request, Context context) {
		logger.info("Start ReservationsPost");
		amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.EU_CENTRAL_1)
			.build();

		persistReservation(request);

		return null;
	}

	private void persistReservation(Reservation reservation) throws ConditionalCheckFailedException {
		Map<String, AttributeValue> attributesMap = new HashMap<>();
		attributesMap.put("tableNumber", new AttributeValue().withN(String.valueOf(reservation.getTableNumber())));
		attributesMap.put("clientName", new AttributeValue().withS(reservation.getClientName()));
		attributesMap.put("phoneNumber", new AttributeValue().withS(reservation.getPhoneNumber()));
		attributesMap.put("date", new AttributeValue().withS(reservation.getDate()));
		attributesMap.put("slotTimeStart", new AttributeValue().withS(reservation.getSlotTimeStart()));
		attributesMap.put("slotTimeEnd", new AttributeValue().withS(reservation.getSlotTimeEnd()));

		logger.info("Persist Reservations");
		amazonDynamoDB.putItem("Reservations", attributesMap);//cmtr-6e999703-Reservations-test
	}
}
