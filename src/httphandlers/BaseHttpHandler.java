package httphandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managing.TaskManager;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class BaseHttpHandler implements HttpHandler {
    protected TaskManager manager;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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

            sendHasInteractions(httpExchange, "Oops");
        } catch (Exception e) {
            sendNotFound(httpExchange, "Error");
        }
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException { // Коды должны быть разными
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        byte[] response = "Задание пересекается с существующими".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(406, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected boolean isNumber(String string) {
        if (string == null) return false;
        else {
            try {
                Integer.parseInt(string);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    protected Integer getIdFromPath(String path) {
        String[] pathArray = path.split("/");
        Optional<Integer> optId = Optional.of(Integer.parseInt(pathArray[3]));
        return optId.get();
    }

    protected String readText(HttpExchange exchange) {
        Gson gson = new Gson();
        String body = exchange.getResponseBody().toString();
        return gson.toJson(exchange);
    }
}
