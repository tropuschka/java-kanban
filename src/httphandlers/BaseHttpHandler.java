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

    protected Task newTask(String parameterString, String type) { // Переместить в отдельный метод?
        String taskName = "New name";
        String taskDescription = "New description";
        String dateString = null;
        String durationString = null;
        String endDateString = null;
        int epicId = -1;
        if (parameterString != null) {
            String[] taskParams = parameterString.split("&");
            for (String item : taskParams) {
                int equalIndex = item.indexOf("=");
                String param = item.substring(0, equalIndex);
                String value = item.substring(equalIndex + 1);
                switch (param) {
                    case "name":
                        taskName = value;
                        break;
                    case "description":
                        taskDescription = value;
                        break;
                    case "start-date":
                        dateString = value;
                        break;
                    case "end-date":
                        endDateString = value;
                        break;
                    case "epic-id":
                        if (isNumber(value)) epicId = Integer.parseInt(value);
                        break;
                }
            }
        }
        if (dateString != null && endDateString != null) {
            LocalDateTime taskStartDate = LocalDateTime.parse(dateString, formatter);
            LocalDateTime taskEndDate = LocalDateTime.parse(endDateString, formatter);
            Duration taskDuration = Duration.between(taskStartDate, taskEndDate);
            durationString = taskDuration.toString();
        }

        switch (type) {
            case "epic":
                return new Epic(-1, taskName, taskDescription);
            case "subtask":
                if (dateString != null)
                    return new Subtask(-1, taskName, taskDescription, epicId, dateString, durationString);
                else return new Subtask(-1, taskName, taskDescription, epicId);
            default:
                if (dateString != null) return new Task(-1, taskName, taskDescription, dateString, durationString);
                else return new Task(-1, taskName, taskDescription);
        }
    }

    protected Task newTask(String parameterString, String type, int taskId) { // Переместить в отдельный метод?
        String taskName = "New name";
        String taskDescription = "New description";
        String dateString = null;
        String durationString = null;
        String endDateString = null;
        int epicId = -1;
        if (parameterString != null) {
            String[] taskParams = parameterString.split("&");
            for (String item : taskParams) {
                int equalIndex = item.indexOf("=");
                String param = item.substring(0, equalIndex);
                String value = item.substring(equalIndex + 1);
                switch (param) {
                    case "name":
                        taskName = value;
                        break;
                    case "description":
                        taskDescription = value;
                        break;
                    case "start-date":
                        dateString = value;
                        break;
                    case "end-date":
                        endDateString = value;
                        break;
                    case "epic-id":
                        if (isNumber(value)) epicId = Integer.parseInt(value);
                        break;
                }
            }
        }
        if (dateString != null && endDateString != null) {
            LocalDateTime taskStartDate = LocalDateTime.parse(dateString, formatter);
            LocalDateTime taskEndDate = LocalDateTime.parse(endDateString, formatter);
            Duration taskDuration = Duration.between(taskStartDate, taskEndDate);
            durationString = taskDuration.toString();
        }

        switch (type) {
            case "epic":
                return new Epic(taskId, taskName, taskDescription);
            case "subtask":
                if (dateString != null)
                    return new Subtask(taskId, taskName, taskDescription, epicId, dateString, durationString);
                else return new Subtask(taskId, taskName, taskDescription, epicId);
            default:
                if (dateString != null) return new Task(taskId, taskName, taskDescription, dateString, durationString);
                else return new Task(taskId, taskName, taskDescription);
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
