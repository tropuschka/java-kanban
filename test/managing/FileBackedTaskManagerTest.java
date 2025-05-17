package managing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file;

    @BeforeEach
    void createFileBackedManager() throws IOException {
        file = File.createTempFile("tasks", ".txt");
        taskManager = new FileBackedTaskManager(file);
        initTasks();
    }

    @Test
    void downloadTMFromFile() {
        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertTrue(taskManager.getAllTasks().equals(taskManagerFromFile.getAllTasks()) &&
                taskManager.getAllEpic().equals(taskManagerFromFile.getAllEpic()) &&
                taskManager.getAllSubtasks().equals(taskManagerFromFile.getAllSubtasks()));
    }

    @AfterEach
    void deleteTemp() {
        file.deleteOnExit();
    }
}
