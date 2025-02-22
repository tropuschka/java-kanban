package managing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    TaskManager fileBackedTM;

    @BeforeEach
    void createManager() {
        fileBackedTM = new FileBackedTaskManager();
    }

    @Test
    void downloadTMFromFile() {
        Task task = new Task(1, "Task", "Some task");
        Epic epic = new Epic(1, "Task", "Some task");
        Subtask subtask = new Subtask(1, "Task", "Some task", 1);
        fileBackedTM.createTask(task);
        fileBackedTM.createEpic(epic);
        fileBackedTM.createSubtask(subtask);
        Path path = Paths.get("C:\\Users\\Galya\\Desktop\\Yandex\\java-kanban\\tasks.txt");
        File file = path.toFile();
        FileBackedTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertTrue(fileBackedTM.getAllTasks().equals(taskManagerFromFile.getAllTasks()) &&
                fileBackedTM.getAllEpic().equals(taskManagerFromFile.getAllEpic()) &&
                fileBackedTM.getAllSubtasks().equals(taskManagerFromFile.getAllSubtasks()));
    }
}
