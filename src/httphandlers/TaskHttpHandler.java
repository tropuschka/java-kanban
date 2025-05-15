package httphandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exceptions.TaskValidationException;
import managing.TaskManager;
import taskmodels.Task;
import typeadapter.DurationTypeAdapter;
import typeadapter.FormatterTypeAdapter;
import typeadapter.LocalDateTypeAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskHttpHandler extends BaseHttpHandler {
    public TaskHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            final String path = exchange.getRequestURI().getPath();
            final Integer requestedId = getIdFromPath(path);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                    .registerTypeAdapter(DateTimeFormatter.class, new FormatterTypeAdapter())
                    .create();

            switch (exchange.getRequestMethod()) {
                case "GET": {
                    if (requestedId == 0 || requestedId == null) {
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
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                case "POST": {
                    String json = readText(exchange);
                    final Task task = gson.fromJson(json, Task.class);
                    final Integer id = task.getId();
                    if (id > 0) {
                        manager.updateTask(task);
                        System.out.println("Задача с айди " + id + " обновлена");
                        sendSuccess(exchange);
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
                        sendSuccess(exchange);
                        return;
                    }

                    final Task task = manager.findTaskById(requestedId);
                    if (task != null) {
                        manager.deleteTask(requestedId);
                        System.out.println("Задача с айди" + requestedId + " удалена");
                        sendSuccess(exchange);
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
