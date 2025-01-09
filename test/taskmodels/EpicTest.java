package taskmodels;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void subtasksAreEqualIfIdIsTheSame() {
        Epic task1 = new Epic(2, "First task", "Common task");
        Epic task2 = new Epic(2, "First task", "Common task");
        assertEquals(task1, task2);
    }

}