package managing;

import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

import java.util.ArrayList;

public interface TaskManager {
    Task createTask(Task task);

    void updateTask(Task task);

    Task deleteTask(Integer id);

    ArrayList<Task> getAllTasks();

    void deleteAllTasks();

    Task findTaskById(Integer id);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(Integer id);

    ArrayList<Epic> getAllEpic();

    void deleteAllEpics();

    Epic findEpicById(Integer id);

    ArrayList<Subtask> getEpicSubtasks(Epic epic);

    Subtask createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(Subtask subtask);

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask findSubtaskById(Integer id);

    ArrayList<Task> getHistory();

    void deleteEpicSubtasks(Epic epic);

    ArrayList<Task> getPrioritizedTasks();
}
