package httphandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managing.TaskManager;
import typeadapter.DurationTypeAdapter;
import typeadapter.FormatterTypeAdapter;
import typeadapter.LocalDateTypeAdapter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class BaseHttpHandler implements HttpHandler {
    protected TaskManager manager;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(DateTimeFormatter.class, new FormatterTypeAdapter())
            .create();

    public BaseHttpHandler() {
        super();
    }

    public BaseHttpHandler(TaskManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            System.out.println("Start");

            sendHasInteractions(httpExchange);
        } catch (Exception e) {
            System.out.println("Miss");
            sendNotFound(httpExchange);
        }
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendSuccess(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] response = "Not Found".getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendServerError(HttpExchange exchange) throws IOException {
        byte[] response = "Internal Server Error".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(500, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        byte[] response = "Not Acceptable".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(406, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected Integer getIdFromPath(String path) {
        String[] pathArray = path.split("/");
        if (pathArray.length >= 3) {
            Optional<Integer> optId = Optional.of(Integer.parseInt(pathArray[2]));
            return optId.get();
        } else {
            return 0;
        }
    }

    protected String readText(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        reader.close();

        return builder.toString();
    }
}
