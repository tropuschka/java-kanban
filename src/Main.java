import managing.*;
import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

public class Main {

    public static void main(String[] args) {

        Managers managers = new Managers();
        InMemoryTaskManager manager = managers.createTaskManager();
        InMemoryHistoryManager historyManager = managers.createHistoryManager();

        Task task1 = new Task(1, "First task", "Common task");
        manager.createTask(task1);
        Task task2 = new Task(2, "Second task", "Common task"); // У него почему-то сквозной айди 4 :)
        manager.createTask(task2);
        Task task3 = new Task(3, "Third task", "Common task");
        manager.createTask(task3);
        Task task4 = new Task(4, "Fourth task", "Common task");
        manager.createTask(task2);
        Epic epic1 = new Epic(5, "First epic", "Epic task");
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask(6, "First subtask", "Small task", 5);
        manager.createSubtask(subtask1);
        Epic epic2 = new Epic(7, "Second epic", "Epic task");
        manager.createEpic(epic2);
        Subtask subtask2 = new Subtask(8, "First subtask", "Small task", 7);
        manager.createSubtask(subtask2);
        Subtask subtask3= new Subtask(9, "Second subtask", "Small task", 7);
        manager.createSubtask(subtask3);
        Epic epic3 = new Epic(10, "Third epic", "Epic task");
        manager.createEpic(epic3);

        manager.findEpicById(5);
        manager.findSubtaskById(6);
        manager.findTaskById(1);
        manager.findTaskById(3);
        manager.findEpicById(7);
        manager.findSubtaskById(8);
        manager.findSubtaskById(9);
        manager.findEpicById(10);
        manager.findTaskById(3);
        manager.findTaskById(4);
        manager.findTaskById(4);

        printAllTasks(manager, historyManager);
    }

    private static void printAllTasks(TaskManager manager, HistoryManager historyManager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpic()) {
            System.out.println(epic);

            for (Subtask task : manager.getEpicSubtasks(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }
}
