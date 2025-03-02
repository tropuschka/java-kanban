package managing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    TaskManager fileBackedTM;
    File file;

    @BeforeEach
    void createManager() throws IOException {
        Task task = new Task(1, "Task", "Some task");
        Epic epic = new Epic(1, "Task", "Some task");
        Subtask subtask = new Subtask(1, "Task", "Some task", 1);
        file = File.createTempFile("tasks", ".txt");
        fileBackedTM = new FileBackedTaskManager(file);
        fileBackedTM.createTask(task);
        fileBackedTM.createEpic(epic);
        fileBackedTM.createSubtask(subtask);
    }

    @Test
    void downloadTMFromFile() {
        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertTrue(fileBackedTM.getAllTasks().equals(taskManagerFromFile.getAllTasks()) &&
                fileBackedTM.getAllEpic().equals(taskManagerFromFile.getAllEpic()) &&
                fileBackedTM.getAllSubtasks().equals(taskManagerFromFile.getAllSubtasks()));
    }

    @AfterEach
    void deleteTemp() {
        file.deleteOnExit();
    }
}
