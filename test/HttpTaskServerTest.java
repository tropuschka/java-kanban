import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private static HttpTaskServer httpServer;

    @BeforeAll
    public static void startAndRunServer() {
        httpServer = new HttpTaskServer();
        httpServer.start();
    }

    @AfterAll
    public static void stopServer() {
        httpServer.stop();
    }

    @Test
    public void createTask() {

        URI url = URI.create("http://localhost:8080/task");

    }
}
