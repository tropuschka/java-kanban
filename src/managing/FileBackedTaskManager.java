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
            fw.write("id,type,name,status,description,epic");
            for (Task task : super.getAllTasks()) {
                String taskString = task.toFile();
                fw.write(taskString + "\n");
            }
            for (Epic epic : super.getAllEpic()) {
                String epicString = epic.toFile();
                fw.write(epicString + "\n");
            }
            for (Subtask subtask : super.getAllSubtasks()) {
                String epicString = subtask.toFile();
                fw.write(epicString + "\n");
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
