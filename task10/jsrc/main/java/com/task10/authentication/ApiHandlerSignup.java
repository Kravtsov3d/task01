package com.task10.authentication;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
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

        List<AttributeType> attributes = new ArrayList<>();
        attributes.add(new AttributeType().withName("custom:firstName").withValue(request.getFirstName()));
        attributes.add(new AttributeType().withName("custom:lastName").withValue(request.getLastName()));

        SignUpRequest signUpRequest = new SignUpRequest()
            .withClientId(request.getClientId())
            .withUsername(request.getEmail())
            .withPassword(request.getPassword())
            .withUserAttributes(attributes);

        logger.info("Start SignUp");
        cognito.signUp(signUpRequest);

        AdminConfirmSignUpRequest confirmSignUpRequest = new AdminConfirmSignUpRequest()
            .withUsername(request.getEmail())
            .withUserPoolId(request.getUserPoolId());

        logger.info("Start ConfirmSignUp");
        cognito.adminConfirmSignUp(confirmSignUpRequest);

        return null;
    }
}
