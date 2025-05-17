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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        httpServer.start();
        System.out.println("Server run");
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        manager.deleteAllTasks();
    }

    @AfterEach
    public void stopServer() {
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

    @Test
    public void getTaskAll() throws IOException, InterruptedException {
        Task task = new Task(0, "Task", "Description");
        Task task2 = new Task(0, "Task", "Description");
        manager.createTask(task);
        manager.createTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> managerTask = manager.getAllTasks();
        JsonElement jElement = JsonParser.parseString(response.body());
        JsonArray jArray = jElement.getAsJsonArray();
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < jArray.size(); i++) {
            JsonElement element = jArray.get(i);
            Task gotTask = gson.fromJson(element, Task.class);
            taskList.add(gotTask);
        }
        assertEquals(managerTask, taskList);


        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(2, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void getEpicAll() throws IOException, InterruptedException {
        Epic task = new Epic(0, "Task", "Description");
        Epic task2 = new Epic(0, "Task", "Description");
        manager.createEpic(task);
        manager.createEpic(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> managerTask = manager.getAllEpic();
        JsonElement jElement = JsonParser.parseString(response.body());
        JsonArray jArray = jElement.getAsJsonArray();
        List<Epic> taskList = new ArrayList<>();
        for (int i = 0; i < jArray.size(); i++) {
            JsonElement element = jArray.get(i);
            Epic gotTask = gson.fromJson(element, Epic.class);
            taskList.add(gotTask);
        }
        assertEquals(managerTask, taskList);

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(2, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void getSubtaskAll() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Task", "Description");
        Epic managerEpic = manager.createEpic(epic);
        Subtask task = new Subtask(0, "Task", "Description", managerEpic.getId());
        Subtask task2 = new Subtask(0, "Task", "Description", managerEpic.getId());
        manager.createSubtask(task);
        manager.createSubtask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());


        List<Subtask> managerTask = manager.getAllSubtasks();
        JsonElement jElement = JsonParser.parseString(response.body());
        JsonArray jArray = jElement.getAsJsonArray();
        List<Subtask> taskList = new ArrayList<>();
        for (int i = 0; i < jArray.size(); i++) {
            JsonElement element = jArray.get(i);
            Subtask gotTask = gson.fromJson(element, Subtask.class);
            taskList.add(gotTask);
        }

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(2, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void createInteractingTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Task", "Description", "2025-05-16 09:00", "PT15M");
        Task task2 = new Task(0, "Task", "Description", "2025-05-16 09:05", "PT15M");
        manager.createTask(task);
        String jTask = gson.toJson(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jTask)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());

        List<Task> managerTask = manager.getAllTasks();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void createInteractingSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Task", "Description");
        Epic managerEpic = manager.createEpic(epic);
        Subtask task = new Subtask(0, "Task", "Description", managerEpic.getId(), "2025-05-16 09:10", "PT15M");
        Subtask task2 = new Subtask(0, "Task", "Description", managerEpic.getId(), "2025-05-16 09:00", "PT15M");
        manager.createSubtask(task2);
        String jTask = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jTask)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());

        List<Subtask> managerTask = manager.getAllSubtasks();

        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Task", "Description");
        Task createdTask = manager.createTask(task);
        Task task1 = new Task(0, "Task", "Description");
        manager.createTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task/" + createdTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> managerTask = manager.getAllTasks();

        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        Epic task = new Epic(0, "Task", "Description");
        Epic createdTask = manager.createEpic(task);
        Epic task1 = new Epic(0, "Task", "Description");
        manager.createEpic(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic/" + createdTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> managerTask = manager.getAllEpic();

        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
    }

    @Test
    public void deleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Task", "Description");
        Epic managerEpic = manager.createEpic(epic);
        Subtask task = new Subtask(0, "Task", "Description", managerEpic.getId());
        Subtask createdTask = manager.createSubtask(task);
        Subtask task1 = new Subtask(0, "Task", "Description", managerEpic.getId());
        manager.createSubtask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask/" + createdTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());


        List<Subtask> managerTask = manager.getAllSubtasks();

        assertEquals(1, managerTask.size(), "Incorrect number of tasks");
    }

    @Test
    public void deleteTaskAll() throws IOException, InterruptedException {
        Task task = new Task(0, "Task", "Description");
        manager.createTask(task);
        Task task1 = new Task(0, "Task", "Description");
        manager.createTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> managerTask = manager.getAllTasks();

        assertEquals(0, managerTask.size(), "Incorrect number of tasks");
    }

    @Test
    public void deleteEpicAll() throws IOException, InterruptedException {
        Epic task = new Epic(0, "Task", "Description");
        manager.createEpic(task);
        Epic task1 = new Epic(0, "Task", "Description");
        manager.createEpic(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> managerTask = manager.getAllEpic();

        assertEquals(0, managerTask.size(), "Incorrect number of tasks");
    }

    @Test
    public void deleteSubtaskAll() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Task", "Description");
        Epic managerEpic = manager.createEpic(epic);
        Subtask task = new Subtask(0, "Task", "Description", managerEpic.getId());
        manager.createSubtask(task);
        Subtask task1 = new Subtask(0, "Task", "Description", managerEpic.getId());
        manager.createSubtask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());


        List<Subtask> managerTask = manager.getAllSubtasks();

        assertEquals(0, managerTask.size(), "Incorrect number of tasks");
    }


    @Test
    public void getNotExistingTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Task", "Description");
        Task createdTask = manager.createTask(task);
        manager.deleteTask(createdTask.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/task/" + createdTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());


        List<Epic> managerTask = manager.getAllEpic();

        assertEquals(0, managerTask.size(), "Incorrect number of tasks");
    }

    @Test
    public void getNotExistingEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Task", "Description");
        Epic createdTask = manager.createEpic(epic);
        manager.deleteEpic(createdTask.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epic/" + createdTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());


        List<Epic> managerTask = manager.getAllEpic();

        assertEquals(0, managerTask.size(), "Incorrect number of tasks");
    }

    @Test
    public void getNotExistingSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Task", "Description");
        Epic managerEpic = manager.createEpic(epic);
        Subtask task = new Subtask(0, "Task", "Description", managerEpic.getId());
        Subtask createdTask = manager.createSubtask(task);
        manager.deleteSubtask(createdTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtask/" + createdTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());


        List<Subtask> managerTask = manager.getAllSubtasks();

        assertEquals(0, managerTask.size(), "Incorrect number of tasks");
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        Task task = new Task(0, "Task", "Description");
        Task createdTask = manager.createTask(task);
        Task task1 = new Task(0, "Task", "Description");
        Task createdTask1 = manager.createTask(task1);
        manager.findTaskById(createdTask.getId());
        manager.findTaskById(createdTask1.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        ArrayList<Task> managerTask = manager.getHistory();
        String jHistory = gson.toJson(managerTask.stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n")));
        assertEquals(jHistory, response.body());


        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(2, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }

    @Test
    public void getPrioritized() throws IOException, InterruptedException {
        Task task = new Task(0, "Task", "Description", "2025-05-16 09:00", "PT15M");
        Task task2 = new Task(0, "Task", "Description", "2025-05-18 09:05", "PT15M");
        manager.createTask(task);
        manager.createTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/priority");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        ArrayList<Task> managerTask = manager.getPrioritizedTasks();
        String jHistory = gson.toJson(managerTask.stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n")));
        assertEquals(jHistory, response.body());


        assertNotNull(managerTask, "Tasks are not back");
        assertEquals(2, managerTask.size(), "Incorrect number of tasks");
        assertEquals("Task", managerTask.getFirst().getName(), "Incorrect task name");
    }
}
