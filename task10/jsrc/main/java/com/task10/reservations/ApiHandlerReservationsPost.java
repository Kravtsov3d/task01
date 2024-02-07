package com.task10.reservations;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@LambdaHandler(
	lambdaName = "api_handler_reservations_post",
	roleName = "api_handler_reservations_post-role"
)
public class ApiHandlerReservationsPost implements RequestHandler<Object, Map<String, Object>> {

	private static final Logger logger = Logger.getLogger(ApiHandlerReservationsPost.class.getName());

	public Map<String, Object> handleRequest(Object request, Context context) {
		logger.info("Start ReservationsPost");

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("statusCode", 200);
		resultMap.put("body", "Hello from Lambda");
		return resultMap;
	}
}
