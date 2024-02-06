package com.task08;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.syndicate.deployment.annotations.LambdaUrlConfig;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.model.ArtifactExtension;
import com.syndicate.deployment.model.DeploymentRuntime;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.logging.Logger;
import org.open.meteo.OpenMeteo;

@LambdaHandler(
	lambdaName = "api_handler",
	roleName = "api_handler-role",
	runtime = DeploymentRuntime.JAVA11,
	layers = "cmtr-6e999703-lambda-layer-test"
)
@LambdaLayer(
	layerName = "cmtr-6e999703-lambda-layer-test",
	libraries = "task08-lib-1.0.0.jar",
	runtime = DeploymentRuntime.JAVA11,
	artifactExtension = ArtifactExtension.JAR
)
@LambdaUrlConfig
public class ApiHandler implements RequestHandler<Object, Map<String, Object>> {

	private static final Logger logger = Logger.getLogger(ApiHandler.class.getName());

	public Map<String, Object> handleRequest(Object request, Context context) {
		logger.info("Start lambda");

		final HttpResponse<String> meteoData = new OpenMeteo().getLatestWeatherForecast();

		Gson gson = new Gson();
		Map<String, Object> data = gson.fromJson(meteoData.body(), Map.class);
		logger.info("data = " + data);
		return data;
	}
}
