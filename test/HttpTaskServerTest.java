import com.google.gson.*;
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
        httpServer.start();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        manager.deleteAllTasks();
    }

    @AfterEach
    public void stopServer() {
        httpServer.stop();
    }

    @Test
    public void createTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Task", "Description");
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

    @Test
    public void createEpic() throws IOException, InterruptedException {
        Epic task = new Epic(0, "Task", "Description");
        String jTask = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jTask)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> managerTask = manager.getAllEpic();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void createSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Task", "Description");
        Epic managerEpic = manager.createEpic(epic);
        Subtask task = new Subtask(0, "Task", "Description", managerEpic.getId());
        String jTask = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jTask)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Subtask> managerTask = manager.getAllSubtasks();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void updateTask() throws IOException, InterruptedException {
        Task task2 = new Task(0, "Task", "Description");
        Task updatingTask = manager.createTask(task2);
        int id = updatingTask.getId();
        Task task = new Task(id, "Task1", "Description");
        String jTask = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jTask)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> managerTask = manager.getAllTasks();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task1", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void updateEpic() throws IOException, InterruptedException {
        Epic task2 = new Epic(0, "Task", "Description");
        Epic updatingTask = manager.createEpic(task2);
        int id = updatingTask.getId();
        Epic task = new Epic(id, "Task1", "Description");
        String jTask = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jTask)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> managerTask = manager.getAllEpic();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task1", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void updateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Task", "Description");
        Epic managerEpic = manager.createEpic(epic);
        Subtask task2 = new Subtask(0, "Task", "Description", managerEpic.getId());
        Subtask managerSub = manager.createSubtask(task2);
        int id = managerSub.getId();
        Subtask task = new Subtask(id, "Task1", "Description", managerEpic.getId());
        String jTask = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jTask)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> managerTask = manager.getAllSubtasks();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task1", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void getTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Task", "Description");
        Task createdTask = manager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task/" + createdTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> managerTask = manager.getAllTasks();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void getEpic() throws IOException, InterruptedException {
        Epic task = new Epic(0, "Task", "Description");
        Epic createdTask = manager.createEpic(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic/" + createdTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> managerTask = manager.getAllEpic();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void getSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Task", "Description");
        Epic managerEpic = manager.createEpic(epic);
        Subtask task = new Subtask(0, "Task", "Description", managerEpic.getId());
        Subtask createdTask = manager.createSubtask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask/" + createdTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());


        List<Subtask> managerTask = manager.getAllSubtasks();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }
}
