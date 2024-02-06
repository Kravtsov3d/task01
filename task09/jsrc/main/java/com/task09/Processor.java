package com.task09;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.syndicate.deployment.annotations.LambdaUrlConfig;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.TracingMode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@LambdaHandler(
    lambdaName = "processor",
    roleName = "processor-role",
    runtime = DeploymentRuntime.JAVA11,
    tracingMode = TracingMode.Active
)
@LambdaUrlConfig
public class Processor implements RequestHandler<Object, Map<String, Object>> {

    private static final Logger logger = Logger.getLogger(Processor.class.getName());
    private AmazonDynamoDB amazonDynamoDB;

    public Map<String, Object> handleRequest(Object request, Context context) {
        logger.info("Init DynamoDB client");
        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

        saveWeather(getMeteoData());

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("statusCode", 200);
        resultMap.put("body", "Hello from Lambda");
        return resultMap;
    }

    private HttpResponse<String> getMeteoData() {
        String apiUrl =
            "https://api.open-meteo.com/v1/forecast?latitude=50.4547&longitude=30.5238&hourly=temperature_2m&timezone=Europe%2FKyiv";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .GET()
            .build();
        HttpResponse<String> response;
        try {
            logger.info("Get meteo data");
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("body = " + response.body().getClass());
            logger.info("body = " + response.body());
            return response;
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException();
        }
    }

    private void saveWeather(HttpResponse<String> meteoData) throws ConditionalCheckFailedException {
        final String id = String.valueOf(UUID.randomUUID());

        Gson gson = new Gson();

        Map<String, Object> data = gson.fromJson(meteoData.body(), Map.class);
        logger.info("data = " + data);

        Map<String, AttributeValue> forecast = new HashMap<>();
        forecast.put("latitude", new AttributeValue().withS(data.get("latitude").toString()));
        forecast.put("longitude", new AttributeValue().withS(data.get("longitude").toString()));
        forecast.put("generationtime_ms", new AttributeValue().withS(data.get("generationtime_ms").toString()));
        forecast.put("utc_offset_seconds", new AttributeValue().withS(data.get("utc_offset_seconds").toString()));
        forecast.put("timezone", new AttributeValue().withS(data.get("timezone").toString()));
        forecast.put("timezone_abbreviation", new AttributeValue().withS(data.get("timezone_abbreviation").toString()));
        forecast.put("elevation", new AttributeValue().withS(data.get("elevation").toString()));

        Map<String, AttributeValue> hourlyUnits = new HashMap<>();
        hourlyUnits.put("time", new AttributeValue().withS(((Map<String, Object>) data.get("hourly_units")).get("time").toString()));
        hourlyUnits.put("temperature_2m", new AttributeValue().withS(((Map<String, Object>) data.get("hourly_units")).get("temperature_2m").toString()));

        forecast.put("hourly_units", new AttributeValue().withM(hourlyUnits));

        Map<String, AttributeValue> hourly = new HashMap<>();
        hourly.put("time", new AttributeValue().withSS(((List<Object>) ((Map<String, Object>) data.get("hourly")).get("time")).stream().map(
            x -> x.toString()).collect(Collectors.toList())));
        hourly.put("temperature_2m", new AttributeValue().withSS(((List<Object>) ((Map<String, Object>) data.get("hourly")).get("temperature_2m")).stream().map(
            x -> x.toString()).collect(Collectors.toSet())));

        forecast.put("hourly", new AttributeValue().withM(hourly));

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("id", new AttributeValue().withS(id));
        attributes.put("forecast", new AttributeValue().withM(forecast));

        logger.info("Save meteo data to Weather table");
        amazonDynamoDB.putItem("cmtr-6e999703-Weather-test", attributes);//cmtr-6e999703-Weather-test
    }
}
