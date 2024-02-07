package com.task10.authentication;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.ListUserPoolClientsRequest;
import com.amazonaws.services.cognitoidp.model.ListUserPoolClientsResult;
import com.amazonaws.services.cognitoidp.model.ListUserPoolsRequest;
import com.amazonaws.services.cognitoidp.model.ListUserPoolsResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.authentication.model.SignupRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@LambdaHandler(
    lambdaName = "api_handler_signup",
    roleName = "api_handler_signup-role"
)
public class ApiHandlerSignup implements RequestHandler<SignupRequest, Void> {

    private static final Logger logger = Logger.getLogger(ApiHandlerSignup.class.getName());

    public Void handleRequest(SignupRequest request, Context context) {
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

        List<AttributeType> attributes = new ArrayList<>();
        attributes.add(new AttributeType().withName("custom:firstName").withValue(request.getFirstName()));
        attributes.add(new AttributeType().withName("custom:lastName").withValue(request.getLastName()));

        SignUpRequest signUpRequest = new SignUpRequest()
            .withClientId(appClientId)
            .withUsername(request.getEmail())
            .withPassword(request.getPassword())
            .withUserAttributes(attributes);

        logger.info("Start SignUp");
        cognito.signUp(signUpRequest);

        AdminConfirmSignUpRequest confirmSignUpRequest = new AdminConfirmSignUpRequest()
            .withUsername(request.getEmail())
            .withUserPoolId(userPoolId);

        logger.info("Start ConfirmSignUp");
        cognito.adminConfirmSignUp(confirmSignUpRequest);

        return null;
    }
}
