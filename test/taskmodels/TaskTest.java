package taskmodels;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void tasksAreEqualIfIdIsTheSame() {
        Task task1 = new Task(1, "First task", "Common task");
        Task task2 = new Task(1, "First task", "Common task");
        assertEquals(task1, task2);
    }
}