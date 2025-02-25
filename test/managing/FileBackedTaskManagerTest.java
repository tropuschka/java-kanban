package managing;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    TaskManager fileBackedTM;
    File file;

    @BeforeEach
    void createManager() throws IOException {
        fileBackedTM = new FileBackedTaskManager();
        FileBackedTaskManager tempTM = new FileBackedTaskManager();
        Task taskT = new Task(1, "Task", "Some task");
        Epic epicT = new Epic(1, "Task", "Some task");
        Subtask subtaskT = new Subtask(1, "Task", "Some task", 1);
        tempTM.createTask(taskT);
        tempTM.createEpic(epicT);
        tempTM.createSubtask(subtaskT);
        file = File.createTempFile("tasks", ".txt");
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write("id,type,name,status,description,epic\n");
            for (Task task : tempTM.getAllTasks()) {
                String taskString = task.toFile();
                fw.write(taskString + "\n");
            }
            for (Epic epic : tempTM.getAllEpic()) {
                String epicString = epic.toFile();
                fw.write(epicString + "\n");
            }
            for (Subtask subtask : tempTM.getAllSubtasks()) {
                String epicString = subtask.toFile();
                fw.write(epicString + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла");
        }
    }

    @Test
    void downloadTMFromFile() {
        Task task = new Task(1, "Task", "Some task");
        Epic epic = new Epic(1, "Task", "Some task");
        Subtask subtask = new Subtask(1, "Task", "Some task", 1);
        fileBackedTM.createTask(task);
        fileBackedTM.createEpic(epic);
        fileBackedTM.createSubtask(subtask);
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
