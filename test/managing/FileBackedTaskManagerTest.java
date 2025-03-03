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
        Epic epic = new Epic(2, "Task", "Some task");
        Subtask subtask = new Subtask(3, "Task", "Some task", 2);
        Task taskWithTime = new Task(4, "Task", "Some task", "01.01.2000 07:00", "PT15M");
        Epic epicWithTime = new Epic(5, "Task", "Some task");
        Subtask subtaskWithTime = new Subtask(6, "Task", "Some task", 5, "02.01.2000 07:00", "PT15M");
        file = File.createTempFile("tasks", ".txt");
        fileBackedTM = new FileBackedTaskManager(file);
        fileBackedTM.createTask(task);
        fileBackedTM.createEpic(epic);
        fileBackedTM.createSubtask(subtask);
        fileBackedTM.createTask(taskWithTime);
        fileBackedTM.createEpic(epicWithTime);
        fileBackedTM.createSubtask(subtaskWithTime);
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
