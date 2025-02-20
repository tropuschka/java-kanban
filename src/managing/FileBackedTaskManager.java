package managing;

import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public void save() {
        try (FileWriter fw = new FileWriter("tasks.txt", StandardCharsets.UTF_8)) {
            for (Task task : super.getAllTasks()) {
                String taskString = task.toString();
                fw.write(taskString + "\n\n");
            }
            for (Epic epic : super.getAllEpic()) {
                String epicString = epic.getWithSubs();
                fw.write(epicString + "\n\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи файла");
        }
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return super.findTaskById(task.getId());
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return super.findEpicById(epic.getId());
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return super.findSubtaskById(subtask.getId());
    }
}
