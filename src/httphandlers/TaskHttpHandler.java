package httphandlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHttpHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String method = exchange.getRequestMethod();
        String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        String[] bodyArray = body.split("/");

        //Здесь будет собственно обработка запросов
    }
}
