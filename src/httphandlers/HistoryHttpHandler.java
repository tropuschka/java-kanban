package httphandlers;

import com.sun.net.httpserver.HttpExchange;
import managing.HistoryManager;
import managing.TaskManager;
import taskmodels.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class HistoryHttpHandler  extends BaseHttpHandler {
    public HistoryHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                    ArrayList<Task> allTasksArray = manager.getHistory();
                    if (allTasksArray.isEmpty()) sendText(exchange, "History is empty");
                    String response = allTasksArray.stream().map(Task::toString).collect(Collectors.joining("\n"));
                    sendText(exchange, response);
                break;
            default:
                sendNotFound(exchange, "Not Found");
        }
    }
}