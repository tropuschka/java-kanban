package httphandlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicHttpHandler  extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String method = exchange.getRequestMethod();
        String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        String[] bodyArray = body.split("/");

        String response;
        switch (method) {
            case "POST":
                response = "post";
            case "GET":
                response = "get";
            case "DELETE":
                response = "delete";
            default:
                sendNotFound(exchange, "Not Found");
        }
    }
}