package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import managing.TaskManager;
import taskmodels.Task;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskHttpHandler extends BaseHttpHandler {
    public TaskHttpHandler(TaskManager manager) {
        super(manager);
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
        } else cutRequest = url;
        String[] requestArray = cutRequest.split("/");
        Task task;

        switch (method) {
            case "POST":
                if (requestArray[1].equals("task") && requestArray.length == 2) {
                    task = newTask(parameterString, "task");
                    manager.createTask(task);
                    if (manager.findTaskById(task.getId()) == null) sendHasInteractions(exchange, "Not Acceptable");
                    sendText(exchange, "Task \"" + task.getName() + "\" created");
                } else if (requestArray[1].equals("task") && requestArray.length == 3 && isNumber(requestArray[2])) {
                    int taskId = Integer.parseInt(requestArray[2]);
                    task = newTask(parameterString, "task", taskId);
                    Task oldTask = manager.findTaskById(taskId);
                    if (oldTask == null) sendNotFound(exchange, "Not Found");
                    manager.updateTask(task);
                    if (Objects.equals(oldTask, manager.findTaskById(taskId)) || manager.findTaskById(taskId) == null) {
                        sendHasInteractions(exchange, "Not Acceptable");
                    }
                    sendText(exchange, "Task \"" + task.getName() + "\" updated");
                } else sendNotFound(exchange, "Not Found");
                break;
            case "GET":
                if (requestArray[1].equals("task") && requestArray.length == 2) {
                    ArrayList<Task> allTasksArray = manager.getAllTasks();
                    if (allTasksArray.isEmpty()) sendNotFound(exchange, "Not Found");
                    String response = allTasksArray.stream().map(Task::toString).collect(Collectors.joining("\n"));
                    sendText(exchange, response);
                } else if (requestArray[1].equals("task") && requestArray.length == 3 && isNumber(requestArray[2])) {
                    int taskId = Integer.parseInt(requestArray[2]);
                    task = manager.findTaskById(taskId);
                    if (task == null) sendNotFound(exchange, "Not found");
                    String response = task.toString();
                    sendText(exchange, response);
                } else sendNotFound(exchange, "Not Found");
                break;
            case "DELETE":
                if (requestArray[1].equals("task") && requestArray.length == 2) {
                    manager.deleteAllTasks();
                    sendText(exchange, "All tasks deleted");
                } else if (requestArray[1].equals("task") && requestArray.length == 3 && isNumber(requestArray[2])) {
                    int taskId = Integer.parseInt(requestArray[2]);
                    if (manager.findTaskById(taskId) == null) sendNotFound(exchange, "Not Found");
                    String taskName = manager.findTaskById(taskId).getName();
                    manager.deleteTask(taskId);
                    sendText(exchange, "Task \"" + taskName + "\" deleted");
                } else sendNotFound(exchange, "Not Found");
                break;
            default:
                sendNotFound(exchange, "Not Found");
        }
    }
}
