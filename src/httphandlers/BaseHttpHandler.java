package httphandlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managing.Managers;
import managing.TaskManager;
import taskmodels.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BaseHttpHandler implements HttpHandler {
    protected TaskManager manager;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public BaseHttpHandler () {
        super();
    }

    public BaseHttpHandler (TaskManager manager) {
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

    protected void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
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

    protected Task newTask(JsonElement jElem) { // Переместить в отдельный метод?
        JsonObject jObj = jElem.getAsJsonObject();
        String taskName = jObj.get("name").getAsString();
        String taskDescription = jObj.get("description").getAsString();
        String dateString = jObj.get("start-date").getAsString();
        String durationString = null;
        if (dateString != null) {
            LocalDateTime taskStartDate = LocalDateTime.parse(dateString, formatter);
            String endDateString = jObj.get("end-date").getAsString();
            LocalDateTime taskEndDate = LocalDateTime.parse(endDateString, formatter);
            Duration taskDuration = Duration.between(taskStartDate, taskEndDate);
            durationString = taskDuration.toString();
        }
        if (dateString != null) return new Task(0, taskName, taskDescription, dateString, durationString);
        else return new Task(0, taskName, taskDescription);
    }

    protected Task newTask(JsonElement jElem, int taskId) { // Переместить в отдельный метод?
        JsonObject jObj = jElem.getAsJsonObject();
        String taskName = jObj.get("name").getAsString();
        String taskDescription = jObj.get("description").getAsString();
        String dateString = jObj.get("start-date").getAsString();
        String durationString = null;
        if (dateString != null) {
            LocalDateTime taskStartDate = LocalDateTime.parse(dateString, formatter);
            String endDateString = jObj.get("end-date").getAsString();
            LocalDateTime taskEndDate = LocalDateTime.parse(endDateString, formatter);
            Duration taskDuration = Duration.between(taskStartDate, taskEndDate);
            durationString = taskDuration.toString();
        }
        if (dateString != null) return new Task(taskId, taskName, taskDescription, dateString, durationString);
        else return new Task(taskId, taskName, taskDescription);
    }
}
