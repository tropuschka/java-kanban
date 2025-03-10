package managing;

import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    Task task;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Task taskWithDeadline;
    Epic epicWithDeadline;
    Subtask subtask1WithDeadline;
    Subtask subtask2WithDeadline;
    Subtask subtask3WithDeadline;

    void initTasks() {
        task = new Task(1, "Task", "Task");
        epic = new Epic(2, "Epic", "Epic description");
        subtask1 = new Subtask(3, "Subtask", "Subtask description", 2);
        subtask2 = new Subtask(4, "Subtask", "Subtask description", 2);
        subtask3 = new Subtask(5, "Subtask", "Subtask description", 2);
        taskWithDeadline = new Task(6, "Task", "Task", "01.01.2000 07:00", "PT15M");
        epicWithDeadline = new Epic(7, "Epic", "Epic description");
        subtask1WithDeadline = new Subtask(8, "Subtask", "Subtask description", 7, "02.01.2000 07:00", "PT15M");
        subtask2WithDeadline = new Subtask(9, "Subtask", "Subtask description", 7, "05.01.2000 07:00", "PT15M");
        subtask3WithDeadline = new Subtask(10, "Subtask", "Subtask description", 7, "04.01.2000 07:00", "PT15M");
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createTask(taskWithDeadline);
        taskManager.createEpic(epicWithDeadline);
        taskManager.createSubtask(subtask1WithDeadline);
        taskManager.createSubtask(subtask2WithDeadline);
        taskManager.createSubtask(subtask3WithDeadline);
    }
}
