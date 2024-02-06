package com.task08;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.LambdaUrlConfig;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;

import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.model.ArtifactExtension;
import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
	lambdaName = "api_handler",
	roleName = "api_handler-role",
	layers = "cmtr-6e999703-lambda-layer-test"
)
@LambdaLayer(
	layerName = "cmtr-6e999703-lambda-layer-test",
	layerFileName = "task08-1.0.0.jar",
	artifactExtension = ArtifactExtension.JAR
)
@LambdaUrlConfig
public class ApiHandler implements RequestHandler<Object, Map<String, Object>> {

	public Map<String, Object> handleRequest(Object request, Context context) {
		System.out.println("Hello from lambda");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("statusCode", 200);
		resultMap.put("body", "Hello from Lambda");
		return resultMap;
	}
}
