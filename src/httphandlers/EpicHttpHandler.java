package httphandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exceptions.TaskValidationException;
import managing.TaskManager;
import taskmodels.Epic;
import typeadapter.DurationTypeAdapter;
import typeadapter.FormatterTypeAdapter;
import typeadapter.LocalDateTypeAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EpicHttpHandler  extends BaseHttpHandler {
    public EpicHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {final String path = exchange.getRequestURI().getPath();
            final Integer requestedId = getIdFromPath(path);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                    .registerTypeAdapter(DateTimeFormatter.class, new FormatterTypeAdapter())
                    .create();

            switch (exchange.getRequestMethod()) {
                case "GET": {
                    if (requestedId == 0 || requestedId == null) {
                        final List<Epic> tasks = manager.getAllEpic();
                        final String response = gson.toJson(tasks);
                        System.out.println("Все эпики получены");
                        sendText(exchange, response);
                        return;
                    }

                    final Epic task = manager.findEpicById(requestedId);
                    if (task != null) {
                        final String response = gson.toJson(task);
                        System.out.println("Эпик с айди " + requestedId + " получен");
                        sendText(exchange, response);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                case "POST": {
                    String json = readText(exchange);
                    final Epic task = gson.fromJson(json, Epic.class);
                    final Integer id = task.getId();
                    if (id > 0) {
                        manager.updateEpic(task);
                        System.out.println("Эпик с айди " + id + " обновлен");
                        sendSuccess(exchange);
                    } else {
                            int newId = manager.createEpic(task).getId();
                            if (manager.findSubtaskById(newId) != null) {
                            System.out.println("Эпик с айди " + newId + " создан");
                            final String response = gson.toJson(task);
                            sendText(exchange, response);
                        } else {
                            System.out.println("Эпик пересекается с существующими");
                            sendHasInteractions(exchange);
                        }
                    }
                    break;
                }
                case "DELETE":{
                    if (requestedId == null) {
                        manager.deleteAllEpics();
                        System.out.println("Все эпики удалены");
                        sendSuccess(exchange);
                        return;
                    }

                    final Epic task = manager.findEpicById(requestedId);
                    if (task != null) {
                        manager.deleteEpic(requestedId);
                        System.out.println("Эпик с айди" + requestedId + " удален");
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