package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import managing.Managers;
import managing.TaskManager;
import taskmodels.Epic;
import taskmodels.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class EpicHttpHandler  extends BaseHttpHandler {
    public EpicHttpHandler(TaskManager manager) {
        super(manager);
    }
    public EpicHttpHandler() {
        super();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI request = exchange.getRequestURI();
        String method = exchange.getRequestMethod();
        String url = request.toString();
        int questionIndex = url.indexOf("?");
        String cutRequest;
        String parameterString = null;
        if (questionIndex != -1) {
            cutRequest = url.substring(0, questionIndex);
            parameterString = url.substring(questionIndex + 1);
        }
        else cutRequest = url;
        String[] requestArray = cutRequest.split("/");
        Epic task;

        switch (method) {
            case "POST":
                if (requestArray[1].equals("epic") && requestArray.length == 2) {
                    task = (Epic) newTask(parameterString, "epic");
                    manager.createEpic(task);
                    if (manager.findEpicById(task.getId()) == null) sendHasInteractions(exchange, "Not Acceptable");
                    sendText(exchange, "Task \"" + task.getName() + "\" created");
                } else if (requestArray[1].equals("epic") && requestArray.length == 3 && isNumber(requestArray[2])) {
                    int taskId = Integer.parseInt(requestArray[2]);
                    task = (Epic) newTask(parameterString, "epic", taskId);
                    Epic oldTask = manager.findEpicById(taskId);
                    if (oldTask == null) sendNotFound(exchange, "Not Found");
                    manager.updateEpic(task);
                    if (Objects.equals(oldTask, manager.findEpicById(taskId)) || manager.findEpicById(taskId) == null) {
                        sendHasInteractions(exchange, "Not Acceptable");
                    }
                    sendText(exchange, "Task \"" + task.getName() + "\" updated");
                } else sendNotFound(exchange, "Not Found");
            case "GET":
                if (requestArray[1].equals("epic") && requestArray.length == 2) {
                    ArrayList<Epic> allTasksArray = manager.getAllEpic();
                    if (allTasksArray.isEmpty()) sendNotFound(exchange, "Not Found");
                    String response = allTasksArray.stream().map(Epic::toString).collect(Collectors.joining("\n"));
                    sendText(exchange, response);
                } else if (requestArray[1].equals("epic") && requestArray.length == 3 && isNumber(requestArray[2])) {
                    int taskId = Integer.parseInt(requestArray[2]);
                    task = manager.findEpicById(taskId);
                    if (task == null) sendNotFound(exchange, "Not found");
                    String response = task.toString();
                    sendText(exchange, response);
                } else sendNotFound(exchange, "Not Found");
            case "DELETE":
                if (requestArray[1].equals("epic") && requestArray.length == 2) {
                    manager.deleteAllEpics();
                    sendText(exchange, "All tasks deleted");
                } else if (requestArray[1].equals("epic") && requestArray.length == 3 && isNumber(requestArray[2])) {
                    int taskId = Integer.parseInt(requestArray[2]);
                    if (manager.findEpicById(taskId) == null) sendNotFound(exchange, "Not Found");
                    String taskName = manager.findEpicById(taskId).getName();
                    manager.deleteEpic(taskId);
                    sendText(exchange, "Task \"" + taskName + "\" deleted");
                } else sendNotFound(exchange, "Not Found");
            default:
                sendNotFound(exchange, "Not Found");
        }
    }
}