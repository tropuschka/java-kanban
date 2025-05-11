package httphandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exceptions.TaskValidationException;
import managing.TaskManager;
import taskmodels.Subtask;
import typeadapter.DurationTypeAdapter;
import typeadapter.FormatterTypeAdapter;
import typeadapter.LocalDateTypeAdapter;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubtaskHttpHandler  extends BaseHttpHandler {
    public SubtaskHttpHandler(TaskManager manager) {
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
                    if (requestedId == null) {
                        final List<Subtask> tasks = manager.getAllSubtasks();
                        final String response = gson.toJson(tasks);
                        System.out.println("Все подзадачи получены");
                        sendText(exchange, response);
                        return;
                    }

                    final Subtask task = manager.findSubtaskById(requestedId);
                    if (task != null) {
                        final String response = gson.toJson(task);
                        System.out.println("Подзадача с айди " + requestedId + " получена");
                        sendText(exchange, response);
                    }
                    break;
                }
                case "POST": {
                    String json = readText(exchange);
                    final Subtask task = gson.fromJson(json, Subtask.class);
                    final Integer id = task.getId();
                    if (id > 0) {
                        manager.updateSubtask(task);
                        System.out.println("Подзадача с айди " + id + " обновлена");
                        exchange.sendResponseHeaders(201, 0);
                    } else {
                        try {
                            int newId = manager.createSubtask(task).getId();
                            System.out.println("Подзадача с айди " + newId + " создана");
                            final String response = gson.toJson(task);
                            sendText(exchange, response);
                        } catch (TaskValidationException e) {
                            System.out.println("Подзадача пересекается с существующими");
                            sendHasInteractions(exchange);
                        }
                    }
                    break;
                }
                case "DELETE":{
                    if (requestedId == null) {
                        manager.deleteAllSubtasks();
                        System.out.println("Все подзадачи удалены");
                        exchange.sendResponseHeaders(201, 0);
                        return;
                    }

                    final Subtask task = manager.findSubtaskById(requestedId);
                    if (task != null) {
                        manager.deleteSubtask(task);
                        System.out.println("Подзадача с айди" + requestedId + " удалена");
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