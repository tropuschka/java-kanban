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
import java.util.HashMap;
import java.util.Map;
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

            sendHasInteractions(httpExchange);
        } catch (Exception e) {
            sendNotFound(httpExchange);
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

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] response = "Not Found".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
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

    protected String readText(HttpExchange exchange, Integer id) throws IOException {
        Gson gson = new Gson();
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryMap = new HashMap<>();
        for (String param : query.split("&")) {
            int equalIndex = param.indexOf("=");
            queryMap.put(param.substring(0, equalIndex), param.substring(equalIndex + 1));
        }
        if (queryMap.get("epic-id") != null && !isNumber(queryMap.get("epic-id"))) {
            System.out.println("Not Acceptable Epic Id");
            sendHasInteractions(exchange);
            return null;
        }
        String type = exchange.getRequestURI().getPath().split("/")[1];
        switch (type) {
            case "epic": {
                Epic task = new Epic(queryMap, id);
                return gson.toJson(task);
            }
            case "subtask": {
                Subtask task = new Subtask(queryMap, id);
                return gson.toJson(task);
            }
            default: {
                Task task = new Task(queryMap, id);
                return gson.toJson(task);
            }
        }
    }
}
