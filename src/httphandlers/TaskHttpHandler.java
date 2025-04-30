package httphandlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import managing.Managers;
import managing.TaskManager;
import taskmodels.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskHttpHandler extends BaseHttpHandler {
    public TaskHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String method = exchange.getRequestMethod();
        String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        Task task;

        Gson gson = new Gson();
        JsonElement jElem = JsonParser.parseString(body);
        JsonObject jObj = jElem.getAsJsonObject(); // Переместить в отдельный метод?
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

        int indexQuestion = body.indexOf("?");
        body = body.substring(0, indexQuestion);
        String[] bodyArray = body.split("/");

        switch (method) {
            case "POST":
                if (bodyArray[1].equals("task") && bodyArray.length == 2) { // Обработать параметры
                    if (dateString != null) task = new Task(0, taskName, taskDescription, dateString, durationString);
                    else task = new Task(0, taskName, taskDescription);
                    manager.createTask(task);
                    sendText(exchange, "Task \"" + taskName + "\" created");
                } else if (bodyArray[1].equals("task") && bodyArray.length == 3 && isNumber(bodyArray[2])) {
                    int taskId = Integer.parseInt(bodyArray[2]);
                    if (dateString != null) task = new Task(taskId, taskName, taskDescription, dateString, durationString);
                    else task = new Task(taskId, taskName, taskDescription);
                    manager.updateTask(task);
                    sendText(exchange, "Task \"" + taskName + "\" updated");
                } else sendNotFound(exchange, "Not Found");
            case "GET":
                if (bodyArray[1].equals("task") && bodyArray.length == 2) { // Обработать параметры
                    ArrayList<Task> allTasksArray = manager.getAllTasks();
                    sendText(exchange, "All tasks");
                } else if (bodyArray[1].equals("task") && bodyArray.length == 3 && isNumber(bodyArray[2])) {
                    int taskId = Integer.parseInt(bodyArray[2]);
                    manager.findTaskById(taskId);
                    sendText(exchange, "Task");
                } else sendNotFound(exchange, "Not Found");
            case "DELETE":
                if (bodyArray[1].equals("task") && bodyArray.length == 2) { // Обработать параметры
                    manager.deleteAllTasks();
                    sendText(exchange, "All tasks deleted");
                } else if (bodyArray[1].equals("task") && bodyArray.length == 3 && isNumber(bodyArray[2])) {
                    int taskId = Integer.parseInt(bodyArray[2]);
                    manager.deleteTask(taskId);
                    sendText(exchange, "Task \"Name\" deleted");
                } else sendNotFound(exchange, "Not Found");
            default:
                sendNotFound(exchange, "Not Found");
        }
    }
}
