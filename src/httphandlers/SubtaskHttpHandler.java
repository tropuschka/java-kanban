package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import managing.Managers;
import managing.TaskManager;
import taskmodels.Epic;
import taskmodels.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubtaskHttpHandler  extends BaseHttpHandler {
    public SubtaskHttpHandler(TaskManager manager) {
        super(manager);
    }
    public SubtaskHttpHandler() {
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
        Subtask task;

        switch (method) {
            case "POST":
                if (requestArray[1].equals("subtask") && requestArray.length == 2) {
                    task = (Subtask) newTask(parameterString, "subtask");
                    manager.createSubtask(task);
                    if (manager.findSubtaskById(task.getId()) == null) sendHasInteractions(exchange, "Not Acceptable");
                    sendText(exchange, "Subtask \"" + task.getName() + "\" created");
                } else if (requestArray[1].equals("subtask") && requestArray.length == 3 && isNumber(requestArray[2])) {
                    int taskId = Integer.parseInt(requestArray[2]);
                    task = (Subtask) newTask(parameterString, "subtask", taskId);
                    Subtask oldTask = manager.findSubtaskById(taskId);
                    if (oldTask == null) sendNotFound(exchange, "Not Found");
                    manager.updateSubtask(task);
                    if (Objects.equals(oldTask, manager.findSubtaskById(taskId)) || manager.findSubtaskById(taskId) == null) {
                        sendHasInteractions(exchange, "Not Acceptable");
                    }
                    sendText(exchange, "Subtask \"" + task.getName() + "\" updated");
                } else sendNotFound(exchange, "Not Found");
            case "GET":
                if (requestArray[1].equals("subtask") && requestArray.length == 2) {
                    ArrayList<Subtask> allTasksArray = manager.getAllSubtasks();
                    if (allTasksArray.isEmpty()) sendNotFound(exchange, "Not Found");
                    String response = allTasksArray.stream().map(Subtask::toString).collect(Collectors.joining("\n"));
                    sendText(exchange, response);
                } else if (requestArray[1].equals("subtask") && requestArray.length == 3 && isNumber(requestArray[2])) {
                    int taskId = Integer.parseInt(requestArray[2]);
                    task = manager.findSubtaskById(taskId);
                    if (task == null) sendNotFound(exchange, "Not found");
                    String response = task.toString();
                    sendText(exchange, response);
                } else sendNotFound(exchange, "Not Found");
            case "DELETE":
                if (requestArray[1].equals("subtask") && requestArray.length == 2) {
                    manager.deleteAllSubtasks();
                    sendText(exchange, "All subtasks deleted");
                } else if (requestArray[1].equals("subtask") && requestArray.length == 3 && isNumber(requestArray[2])) {
                    int taskId = Integer.parseInt(requestArray[2]);
                    if (manager.findSubtaskById(taskId) == null) sendNotFound(exchange, "Not Found");
                    task = manager.findSubtaskById(taskId);
                    String taskName = manager.findSubtaskById(taskId).getName();
                    manager.deleteSubtask(task);
                    sendText(exchange, "Subtask \"" + taskName + "\" deleted");
                } else sendNotFound(exchange, "Not Found");
            default:
                sendNotFound(exchange, "Not Found");
        }
    }
}