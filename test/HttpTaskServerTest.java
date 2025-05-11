import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managing.InMemoryTaskManager;
import org.junit.jupiter.api.*;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;
import typeadapter.DurationTypeAdapter;
import typeadapter.FormatterTypeAdapter;
import typeadapter.LocalDateTypeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private static HttpTaskServer httpServer;
    private static InMemoryTaskManager manager = new InMemoryTaskManager();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(DateTimeFormatter.class, new FormatterTypeAdapter())
            .create();

    @BeforeAll
    public static void startAndRunServer() throws IOException {
        httpServer = new HttpTaskServer(manager);
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        manager.deleteAllTasks();
        httpServer.start();
    }

    @AfterEach
    public void stopServer() {
        httpServer.stop();
    }

    @Test
    public void createTask() throws IOException, InterruptedException {
        Task task = new Task(1, "Task", "Description");
        String jTask = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jTask)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> managerTask = manager.getAllTasks();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }
}
