package managing;

import taskmodels.Epic;
import taskmodels.Subtask;
import taskmodels.Task;
import taskmodels.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public void save() {
        try (FileWriter fw = new FileWriter("tasks.txt", StandardCharsets.UTF_8)) {
            fw.write("id,type,name,status,description,epic\n");
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
            System.out.println(e.getMessage());
        }
    }

    public static Task fromString(String taskString) {
        String[] taskList = taskString.split(",");
        Task task;
        if (taskList[1].equals("EPIC")) {
            task = new Epic(Integer.parseInt(taskList[0]), taskList[2], taskList[4]);
        } else if (taskList[1].equals("SUBTASK")) {
            task = new Subtask(Integer.parseInt(taskList[0]), taskList[2], taskList[4], Integer.parseInt(taskList[5]));
        } else {
            task = new Task(Integer.parseInt(taskList[0]), taskList[2], taskList[4]);
        }
        task.setStatus(TaskStatus.valueOf(taskList[3]));
        return task;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTM = new FileBackedTaskManager();
        try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                String line = br.readLine();
                Task task = null;
                if (!line.split(",")[0].equals("id")) {
                    task = fromString(line);
                    if (line.split(",")[1].equals("EPIC")) fileBackedTM.createEpic((Epic) task);
                    else if (line.split(",")[1].equals("SUBTASK")) fileBackedTM.createSubtask((Subtask) task);
                    else fileBackedTM.createTask(task);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return fileBackedTM;
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
