package com.task08;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.syndicate.deployment.annotations.LambdaUrlConfig;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;

import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.model.ArtifactExtension;
import com.syndicate.deployment.model.DeploymentRuntime;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.logging.Logger;

@LambdaHandler(
	lambdaName = "api_handler",
	roleName = "api_handler-role",
	runtime = DeploymentRuntime.JAVA11,
	layers = "cmtr-6e999703-lambda-layer-test"
)
//@LambdaLayer(
//	layerName = "cmtr-6e999703-lambda-layer-test",
//	layerFileName = "task08-1.0.0.jar",
//	artifactExtension = ArtifactExtension.JAR
//)
@LambdaUrlConfig
public class ApiHandler implements RequestHandler<Object, Map<String, Object>> {

	private static final Logger logger = Logger.getLogger(ApiHandler.class.getName());

	public Map<String, Object> handleRequest(Object request, Context context) {
		logger.info("Start lambda");

		final HttpResponse<String> meteoData = getMeteoData();

		Gson gson = new Gson();
		Map<String, Object> data = gson.fromJson(meteoData.body(), Map.class);
		logger.info("data = " + data);
		return data;
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
			logger.info("body = " + response.body());
			return response;
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException();
		}
	}
}
