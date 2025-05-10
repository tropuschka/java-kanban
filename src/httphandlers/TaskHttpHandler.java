package httphandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.TaskValidationException;
import managing.TaskManager;
import taskmodels.Task;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskHttpHandler extends BaseHttpHandler {
    public TaskHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String path = exchange.getRequestURI().getPath();
        final Integer requestedId = getIdFromPath(path);

        Gson gson = new Gson();

        switch (exchange.getRequestMethod()) {
            case "GET": {
                if (requestedId == null) {
                    final List<Task> tasks = manager.getAllTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Все задания получены");
                    sendText(exchange, response);
                    return;
                }

                final Task task = manager.findTaskById(requestedId);
                if (task != null) {
                    final String response = gson.toJson(task);
                    System.out.println("Задание с айди " + requestedId + " получено");
                    sendText(exchange, response);
                }
                break;
            }
            case "POST": {
                String json = readText(exchange, requestedId);
                final Task task = gson.fromJson(json, Task.class);
                final Integer id = task.getId();
                if (id > 0) {
                    manager.updateTask(task);
                    System.out.println("Задание с айди " + id + " обновлено");
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    try {
                        int newId = manager.createTask(task).getId();
                        System.out.println("Задание с айди " + newId + " создано");
                        final String response = gson.toJson(task);
                        sendText(exchange, response);
                    } catch (TaskValidationException e) {
                        System.out.println("Задача пересекается с существующими");
                        sendHasInteractions(exchange);
                    }
                }
                break;
            }
            case "DELETE":{
                if (requestedId == null) {
                    manager.deleteAllTasks();
                    System.out.println("Все задания удалены");
                    sendText(exchange, "All tasks deleted");
                    return;
                }

                final Task task = manager.findTaskById(requestedId);
                if (task != null) {
                    manager.deleteTask(requestedId);
                    System.out.println("Задание с айди" + requestedId + " удалено");
                    sendText(exchange, "Task deleted");
                }
                break;
            }
            default: {
                 sendNotFound(exchange, "Not Found");
            }
        }
    }
}
