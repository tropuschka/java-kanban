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
import java.util.Objects;

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

        int indexQuestion = body.indexOf("?");
        body = body.substring(0, indexQuestion);
        String[] bodyArray = body.split("/");

        switch (method) {
            case "POST":
                if (bodyArray[1].equals("task") && bodyArray.length == 2) {
                    task = newTask(jElem);
                    manager.createTask(task);
                    if (manager.findTaskById(task.getId()) == null) sendHasInteractions(exchange, "Not Acceptable");
                    sendText(exchange, "Task \"" + task.getName() + "\" created");
                } else if (bodyArray[1].equals("task") && bodyArray.length == 3 && isNumber(bodyArray[2])) {
                    int taskId = Integer.parseInt(bodyArray[2]);
                    task = newTask(jElem, taskId);
                    Task oldTask = manager.findTaskById(taskId);
                    if (oldTask == null) sendNotFound(exchange, "Not Found");
                    manager.updateTask(task);
                    if (Objects.equals(oldTask, manager.findTaskById(taskId)) || manager.findTaskById(taskId) == null) {
                        sendHasInteractions(exchange, "Not Acceptable");
                    }
                    sendText(exchange, "Task \"" + task.getName() + "\" updated");
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
