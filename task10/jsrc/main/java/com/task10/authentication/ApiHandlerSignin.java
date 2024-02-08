package com.task10.authentication;

import static com.task10.util.Util.convertFromJson;
import static com.task10.util.Util.convertToJson;

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
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.authentication.model.SigninRequest;
import com.task10.authentication.model.SigninResponse;
import java.util.HashMap;
import java.util.logging.Logger;

@LambdaHandler(
    lambdaName = "api_handler_signin",
    roleName = "api_handler_signin-role"
)
public class ApiHandlerSignin implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = Logger.getLogger(ApiHandlerSignin.class.getName());

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        logger.info("Start Signin");
        logger.info("request = " + request);
        final SigninRequest signinRequest = convertFromJson(request.getBody(), SigninRequest.class);

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
        parameters.put("USERNAME", signinRequest.getEmail());
        parameters.put("PASSWORD", signinRequest.getPassword());

        InitiateAuthRequest signInRequest = new InitiateAuthRequest()
            .withClientId(appClientId)
            .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
            .withAuthParameters(parameters);

        final InitiateAuthResult result;
        try {
            logger.info("Start SignIn");
            result = cognito.initiateAuth(signInRequest);
            logger.info("result = " + result);
        } catch (Exception e) {
            logger.info("Exception: " + e);
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(400);
            return response;
        }

        final String token = result.getAuthenticationResult().getIdToken();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setBody(convertToJson(new SigninResponse(token)));
        return response;
    }
}
