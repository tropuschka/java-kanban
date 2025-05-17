package taskmodels;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    Epic epic = new Epic(1, "Epic", "Epic task");

    @Test
    void subtasksAreEqualIfIdIsTheSame() {
        Subtask task1 = new Subtask(2, "First task", "Common task", 1);
        Subtask task2 = new Subtask(2, "First task", "Common task", 1);
        assertEquals(task1, task2);
    }
}