package httphandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managing.TaskManager;
import taskmodels.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryHttpHandler  extends BaseHttpHandler {
    public HistoryHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            Gson gson = new Gson();

            switch (method) {
                case "GET":
                    final List<Task> allTasksArray = manager.getHistory();
                    if (!allTasksArray.isEmpty()) {
                        String response = gson.toJson(allTasksArray.stream()
                                .map(Task::toString)
                                .collect(Collectors.joining("\n")));
                        sendText(exchange, response);
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            System.out.println(e);
            sendServerError(exchange);
        }
    }
}