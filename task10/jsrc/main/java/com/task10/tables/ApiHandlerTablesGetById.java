package com.task10.tables;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.task10.tables.model.Table;
import java.util.logging.Logger;

@LambdaHandler(
    lambdaName = "api_handler_tables_get_by_id",
    roleName = "api_handler_tables_get_by_id-role"
)
public class ApiHandlerTablesGetById implements RequestHandler<APIGatewayProxyRequestEvent, Table> {

    private static final Logger logger = Logger.getLogger(ApiHandlerTablesGetById.class.getName());

    public Table handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        logger.info("Start TablesGetById");

        String tableId = event.getPathParameters().get("tableId");
        logger.info("tableId = " + tableId);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        logger.info("Load from Tables");
        final Table result = mapper.load(Table.class, Integer.valueOf(tableId));
        logger.info("result = " + result);
        return result;
    }
}
