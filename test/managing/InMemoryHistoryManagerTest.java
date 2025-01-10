package managing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import taskmodels.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static InMemoryHistoryManager manager;

    @BeforeAll
    static void historyManagerCreation() {
        manager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task1", "Task description");
        Task task2 = new Task(2, "Task2", "Task description");
        Task task3 = new Task(3, "Task3", "Task description");
        Task task4 = new Task(4, "Task4", "Task description");
        Task task5 = new Task(5, "Task5", "Task description");
        Task task6 = new Task(6, "Task6", "Task description");
        Task task7 = new Task(7, "Task7", "Task description");
        Task task8 = new Task(8, "Task8", "Task description");
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(task4);
        manager.add(task5);
        manager.add(task6);
        manager.add(task7);
        manager.add(task8);
    }

    @Test
    void returningHistory() {
        ArrayList<Task> taskArray = new ArrayList<>();
        Task task1 = new Task(1, "Task1", "Task description");
        Task task2 = new Task(2, "Task2", "Task description");
        Task task3 = new Task(3, "Task3", "Task description");
        Task task4 = new Task(4, "Task4", "Task description");
        Task task5 = new Task(5, "Task5", "Task description");
        Task task6 = new Task(6, "Task6", "Task description");
        Task task7 = new Task(7, "Task7", "Task description");
        Task task8 = new Task(8, "Task8", "Task description");
        taskArray.add(task1);
        taskArray.add(task2);
        taskArray.add(task3);
        taskArray.add(task4);
        taskArray.add(task5);
        taskArray.add(task6);
        taskArray.add(task7);
        taskArray.add(task8);
        assertEquals(manager.getHistory(), taskArray);
    }

    @Test
    void ifHistoryMaxSize10() {
        Task task9 = new Task(9, "Task9", "Task description");
        Task task10 = new Task(10, "Task10", "Task description");
        Task task11 = new Task(11, "Task11", "Task description");
        manager.add(task9);
        manager.add(task10);
        manager.add(task11);
        ArrayList<Task> history = manager.getHistory();
        assertEquals(history.size(), 10);
    }
}