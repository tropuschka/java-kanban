package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import managing.TaskManager;
import taskmodels.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtaskHttpHandler  extends BaseHttpHandler {
    public SubtaskHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            final String path = exchange.getRequestURI().getPath();
            final Integer requestedId = getIdFromPath(path);

            switch (exchange.getRequestMethod()) {
                case "GET": {
                    if (requestedId == 0 || requestedId == null) {
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
                    } else {
                        sendNotFound(exchange);
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
                        sendSuccess(exchange);
                    } else {
                        int newId = manager.createSubtask(task).getId();
                        if (manager.findSubtaskById(newId) != null) {
                            System.out.println("Подзадача с айди " + newId + " создана");
                            final String response = gson.toJson(task);
                            sendText(exchange, response);
                        } else {
                            System.out.println("Подзадача пересекается с существующими");
                            sendHasInteractions(exchange);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (requestedId == null || requestedId == 0) {
                        manager.deleteAllSubtasks();
                        System.out.println("Все подзадачи удалены");
                        sendSuccess(exchange);
                        return;
                    }

                    final Subtask task = manager.findSubtaskById(requestedId);
                    if (task != null) {
                        manager.deleteSubtask(task);
                        System.out.println("Подзадача с айди " + requestedId + " удалена");
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