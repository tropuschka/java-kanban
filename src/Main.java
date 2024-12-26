import managing.TaskManager;
import taskmodels.TaskStatus;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task1 = new Task(1, "First task", "A common task");
        manager.createTask(task1);
        Task task2 = new Task(2, "Second task", "A common task");
        manager.createTask(task2);

        Epic epic1 = new Epic(1, "First epic", "A complicated task with two subtasks");
        manager.createEpic(epic1);
        Subtask subtask11 = new Subtask(1, "First subtask", "The first subtask of the first epic", 1);
        manager.createSubtask(subtask11);
        Subtask subtask12 = new Subtask(2, "Second subtask", "The second subtask of the first epic", 1);
        manager.createSubtask(subtask12);

        Epic epic2 = new Epic(2, "Second epic", "A complicated task with one subtask");
        manager.createEpic(epic2);
        Subtask subtask21 = new Subtask(3, "First subtask", "The first subtask of the second epic", 2);
        manager.createSubtask(subtask21);

        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtasks());
        System.out.println(manager.getAllTasks());

        task1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task1);
        task2.setStatus(TaskStatus.DONE);
        manager.updateTask(task2);
        subtask11.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask11);
        subtask21.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask21);

        System.out.println(manager.findTaskById(task1.getId()));
        System.out.println(manager.findTaskById(task2.getId()));
        System.out.println(manager.findEpicById(epic1.getId()));
        System.out.println(manager.getEpicSubtasks(epic1));
        System.out.println(manager.findEpicById(epic2.getId()));

        manager.deleteTask(2);
        manager.deleteEpic(2);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtasks());
    }
}
