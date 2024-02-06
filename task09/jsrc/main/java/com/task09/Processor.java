package com.task09;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.LambdaUrlConfig;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.TracingMode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

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

	private String getMeteoData() {
		String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=50.4547&longitude=30.5238&hourly=temperature_2m&timezone=Europe%2FKyiv";

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(apiUrl))
			.GET()
			.build();
		HttpResponse<String> response;
		try {
			logger.info("Get meteo data");
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			logger.info("body = " + response.body());
			return response.body();
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException();
		}
	}

	private void saveWeather(String meteoData) throws ConditionalCheckFailedException {
		final String id = String.valueOf(UUID.randomUUID());

		Map<String, AttributeValue> attributes = new HashMap<>();
		attributes.put("id", new AttributeValue().withS(id));
		attributes.put("forecast", new AttributeValue().withS(meteoData));

		logger.info("Save meteo data to Weather table");
		amazonDynamoDB.putItem("cmtr-6e999703-Weather-test", attributes);
	}
}
