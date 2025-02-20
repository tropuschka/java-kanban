package managing;

import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public void save() {

    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }
}
