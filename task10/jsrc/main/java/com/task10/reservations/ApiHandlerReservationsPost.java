package com.task10.reservations;

import static com.task10.util.Util.convertFromJson;
import static com.task10.util.Util.convertToJson;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.reservations.model.Reservation;
import com.task10.tables.model.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@LambdaHandler(
    lambdaName = "api_handler_reservations_post",
    roleName = "api_handler_reservations_post-role"
)
public class ApiHandlerReservationsPost implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = Logger.getLogger(ApiHandlerReservationsPost.class.getName());
    private AmazonDynamoDB client;

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        logger.info("Start ReservationsPost");
        logger.info("request = " + request);

        final Reservation reservation = convertFromJson(request.getBody(), Reservation.class);
        logger.info("reservation = " + reservation);

        client = AmazonDynamoDBClientBuilder.defaultClient();


        PaginatedScanList<Table> tables = getTables(reservation);
        if (tables.isEmpty()) {
            logger.warning("tables.isEmpty() = " + tables.isEmpty());
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(400);
            return response;
        }

        PaginatedScanList<Reservation> reservations = getReservations(reservation);
        if (!reservations.isEmpty()) {
            logger.warning("reservations.isEmpty() = " + reservations.isEmpty());
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(400);
            return response;
        }

        final String reservationId = UUID.randomUUID().toString();
        reservation.setId(reservationId);
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        try {
            logger.info("Start save");
            mapper.save(reservation);
        } catch (Exception e) {
            logger.warning("Exception: " + e);
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(400);
            return response;
        }

        final Map<String, String> result = new HashMap<>();
        result.put("reservationId", reservationId);

        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setStatusCode(200);
        responseEvent.setBody(convertToJson(result));
        return responseEvent;
    }

    private PaginatedScanList<Table> getTables(final Reservation reservation) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":numberVal", new AttributeValue().withN(Integer.toString(reservation.getTableNumber())));

        Map<String, String> ean = new HashMap<>();
        ean.put("#numberAttr", "number");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("#numberAttr = :numberVal")
            .withExpressionAttributeValues(eav)
            .withExpressionAttributeNames(ean);

        DynamoDBMapper mapper = new DynamoDBMapper(client);

        logger.info("Start Table scan");
        return mapper.scan(Table.class, scanExpression);
    }

    private PaginatedScanList<Reservation> getReservations(final Reservation reservation) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":tableNumberVal", new AttributeValue().withN(Integer.toString(reservation.getTableNumber())));
        eav.put(":dateVal", new AttributeValue().withS(reservation.getDate()));

        Map<String, String> ean = new HashMap<>();
        ean.put("#tableNumberAttr", "tableNumber");
        ean.put("#dateAttr", "date");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("#tableNumberAttr = :tableNumberVal and #dateAttr = :dateVal")
            .withExpressionAttributeValues(eav)
            .withExpressionAttributeNames(ean);

        DynamoDBMapper mapper = new DynamoDBMapper(client);

        logger.info("Start Reservation scan");
        return mapper.scan(Reservation.class, scanExpression);
    }
}
