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
    void createTask() {
        Task task = new Task(1, "Task", "Some task");
        fileBackedTM.createTask(task);
        ArrayList taskArray = new ArrayList();
        taskArray.add(task);
        assertEquals(taskArray, fileBackedTM.getAllTasks());
    }

    @Test
    void createEpic() {
        Epic task = new Epic(1, "Task", "Some task");
        fileBackedTM.createEpic(task);
        ArrayList taskArray = new ArrayList();
        taskArray.add(task);
        assertEquals(taskArray, fileBackedTM.getAllEpic());
    }

    @Test
    void createSubtask() {
        Epic task = new Epic(1, "Task", "Some task");
        fileBackedTM.createEpic(task);
        Subtask subtask = new Subtask(2, "Subtask", "Some task", 1);
        fileBackedTM.createSubtask(subtask);
        ArrayList taskArray = new ArrayList();
        taskArray.add(subtask);
        assertEquals(taskArray, fileBackedTM.getAllSubtasks());
    }

    @Test
    void downloadTMFromFile() {
        Task task = new Task(1, "Task", "Some task");
        fileBackedTM.createTask(task);
        Path path = Paths.get("C:\\Users\\Galya\\Desktop\\Yandex\\java-kanban\\tasks.txt");
        File file = path.toFile();
        InMemoryTaskManager taskManagerFromFile = FileBackedTaskManager.loadFromFile(file);
        assertEquals(fileBackedTM.getAllTasks(), taskManagerFromFile.getAllTasks());
    }
}
