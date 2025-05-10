package httphandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import managing.TaskManager;
import taskmodels.Task;
import typeadapter.DurationTypeAdapter;
import typeadapter.LocalDateTypeAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PriorityHttpHandler  extends BaseHttpHandler {
    public PriorityHttpHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                    .create();

            switch (method) {
                case "GET":
                    List<Task> allTasksArray = manager.getPrioritizedTasks();
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