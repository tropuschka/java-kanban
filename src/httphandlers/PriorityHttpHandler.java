package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import managing.TaskManager;
import taskmodels.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PriorityHttpHandler  extends BaseHttpHandler {
    public PriorityHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                ArrayList<Task> allTasksArray = manager.getPrioritizedTasks();
                if (allTasksArray.isEmpty()) sendNotFound(exchange, "Not Found");
                String response = allTasksArray.stream().map(Task::toString).collect(Collectors.joining("\n"));
                sendText(exchange, response);
                break;
            default:
                sendNotFound(exchange, "Not Found");
        }
    }
}