package com.task10.authentication;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.ListUserPoolClientsRequest;
import com.amazonaws.services.cognitoidp.model.ListUserPoolClientsResult;
import com.amazonaws.services.cognitoidp.model.ListUserPoolsRequest;
import com.amazonaws.services.cognitoidp.model.ListUserPoolsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.authentication.model.SigninRequest;
import com.task10.authentication.model.SigninResponse;
import java.util.HashMap;
import java.util.logging.Logger;

@LambdaHandler(
	lambdaName = "api_handler_signin",
	roleName = "api_handler_signin-role"
)
public class ApiHandlerSignin implements RequestHandler<SigninRequest, SigninResponse> {

	private static final Logger logger = Logger.getLogger(ApiHandlerSignin.class.getName());

	public SigninResponse handleRequest(SigninRequest  request, Context context) {
		logger.info("request = " + request);

		AWSCognitoIdentityProvider cognito = AWSCognitoIdentityProviderClientBuilder.defaultClient();

		ListUserPoolsRequest listUserPoolsRequest = new ListUserPoolsRequest().withMaxResults(10);
		ListUserPoolsResult userPool = cognito.listUserPools(listUserPoolsRequest);
		String userPoolId = userPool.getUserPools().get(0).getId();
		logger.info("userPoolId = " + userPoolId);

		ListUserPoolClientsRequest userPoolClientsRequest = new ListUserPoolClientsRequest().withUserPoolId(userPoolId);
		ListUserPoolClientsResult userPoolClientsResult = cognito.listUserPoolClients(userPoolClientsRequest);

		String appClientId = userPoolClientsResult.getUserPoolClients().get(0).getClientId();
		logger.info("appClientId = " + appClientId);

		final HashMap<String, String> parameters = new HashMap<>();
		parameters.put("USERNAME", request.getEmail());
		parameters.put("PASSWORD", request.getPassword());

		InitiateAuthRequest signInRequest = new InitiateAuthRequest()
			.withClientId(appClientId)
			.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
			.withAuthParameters(parameters);

		logger.info("Start SignIn");
		final InitiateAuthResult result = cognito.initiateAuth(signInRequest);
		return new SigninResponse(result.getAuthenticationResult().getIdToken());
	}
}
