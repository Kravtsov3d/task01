package com.task05;

import static java.util.stream.Collectors.toMap;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;

import com.task05.model.Event;
import com.task05.model.EventRequest;
import com.task05.model.EventResponse;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.apache.http.HttpStatus;

@LambdaHandler(
    lambdaName = "api_handler",
    roleName = "api_handler-role"
)
public class ApiHandler implements RequestHandler<EventRequest, EventResponse> {

    private static final Logger logger = Logger.getLogger(ApiHandler.class.getName());

    private AmazonDynamoDB amazonDynamoDB;

    public EventResponse handleRequest(EventRequest request, Context context) {
        logger.info("Init DynamoDB client");
        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

        final Event event = createEvent(request);
        persistEvent(event);

        logger.info("Send response");
        return new EventResponse(HttpStatus.SC_CREATED, event);
    }

    private static Event createEvent(final EventRequest request) {
        final String id = String.valueOf(UUID.randomUUID());
        final String createdAt = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        return new Event(
            id,
            request.getPrincipalId(),
            createdAt,
            request.getContent()
        );
    }

    private void persistEvent(Event event) throws ConditionalCheckFailedException {
        Map<String, AttributeValue> attributesMap = new HashMap<>();
        attributesMap.put("id", new AttributeValue().withS(event.getId()));
        attributesMap.put("principalId", new AttributeValue().withN(String.valueOf(event.getPrincipalId())));
        attributesMap.put("createdAt", new AttributeValue().withS(event.getCreatedAt()));
        attributesMap.put("body", new AttributeValue().withM(convertBody(event.getBody())));

        logger.info("Persist event");
        amazonDynamoDB.putItem("cmtr-6e999703-Events-test", attributesMap);
    }

    private static Map<String, AttributeValue> convertBody(final Map<String, String> body) {
        return body.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, e -> new AttributeValue().withS(e.getValue())));
    }
}
