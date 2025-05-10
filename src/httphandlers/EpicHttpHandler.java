package httphandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.TaskValidationException;
import managing.TaskManager;
import taskmodels.Epic;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EpicHttpHandler  extends BaseHttpHandler {
    public EpicHttpHandler(TaskManager manager) {
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
                }
                break;
            }
            case "POST": {
                String json = readText(exchange, requestedId);
                final Epic task = gson.fromJson(json, Epic.class);
                final Integer id = task.getId();
                if (id > 0) {
                    manager.updateEpic(task);
                    System.out.println("Эпик с айди " + id + " обновлен");
                    exchange.sendResponseHeaders(201, 0);
                } else {
                    try {
                        int newId = manager.createEpic(task).getId();
                        System.out.println("Эпик с айди " + newId + " создан");
                        final String response = gson.toJson(task);
                        sendText(exchange, response);
                    } catch (TaskValidationException e) {
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
                    exchange.sendResponseHeaders(201, 0);
                    return;
                }

                final Epic task = manager.findEpicById(requestedId);
                if (task != null) {
                    manager.deleteEpic(requestedId);
                    System.out.println("Эпик с айди" + requestedId + " удален");
                    exchange.sendResponseHeaders(201, 0);
                }
                break;
            }
            default: {
                sendNotFound(exchange);
            }
        }
    }
}