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
        try {
            final String path = exchange.getRequestURI().getPath();
            final Integer requestedId = getIdFromPath(path);

            Gson gson = new Gson();

            switch (exchange.getRequestMethod()) {
                case "GET": {
                    if (requestedId == null) {
                        final List<Task> tasks = manager.getAllTasks();
                        final String response = gson.toJson(tasks);
                        System.out.println("Все задачи получены");
                        sendText(exchange, response);
                        return;
                    }

                    final Task task = manager.findTaskById(requestedId);
                    if (task != null) {
                        final String response = gson.toJson(task);
                        System.out.println("Задача с айди " + requestedId + " получена");
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
                        System.out.println("Задача с айди " + id + " обновлена");
                        exchange.sendResponseHeaders(201, 0);
                    } else {
                        try {
                            int newId = manager.createTask(task).getId();
                            System.out.println("Задача с айди " + newId + " создана");
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
                        System.out.println("Все задачи удалены");
                        exchange.sendResponseHeaders(201, 0);
                        return;
                    }

                    final Task task = manager.findTaskById(requestedId);
                    if (task != null) {
                        manager.deleteTask(requestedId);
                        System.out.println("Задача с айди" + requestedId + " удалена");
                        exchange.sendResponseHeaders(201, 0);
                    }
                    break;
                }
                default: {
                    sendNotFound(exchange);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            sendServerError(exchange);
        }
    }
}
