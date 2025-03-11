package managing;

import exceptions.ManagerSaveException;
import taskmodels.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager() {
        this.file = new File("tasks.txt");
    }

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            fw.write("id,type,name,status,description,start date,end date,epic\n");
            for (Task task : super.getAllTasks()) {
                String taskString = task.toFile();
                fw.write(taskString + "\n");
            }
            for (Epic epic : super.getAllEpic()) {
                String epicString = epic.toFile();
                fw.write(epicString + "\n");
            }
            for (Subtask subtask : super.getAllSubtasks()) {
                String subtaskString = subtask.toFile();
                fw.write(subtaskString + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTM = new FileBackedTaskManager(file);
        try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8); BufferedReader br = new BufferedReader(fr)) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                Task task;
                String typeString = line.split(",")[1];
                TaskType type = TaskType.valueOf(typeString);
                if (!line.split(",")[0].equals("id")) {
                    task = TaskFromString.fromString(line);
                    if (type.equals(TaskType.EPIC)) fileBackedTM.epics.put(task.getId(), (Epic) task);
                    else if (type.equals(TaskType.SUBTASK)) {
                        fileBackedTM.subtasks.put(task.getId(), (Subtask) task);
                        Epic epic = fileBackedTM.epics.get(((Subtask) task).getEpicId());
                        epic.addSubtask(task.getId());
                        if (task.getDuration() != null) epic.addDuration(task.getDuration());
                    } else fileBackedTM.commonTasks.put(task.getId(), task);
                    if ((task.getType().equals(TaskType.SUBTASK) || task.getType().equals(TaskType.TASK))
                        && task.getStartTime() != null) fileBackedTM.sortedTasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки менеджера из файла");
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
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task deleteTask(Integer id) {
        super.deleteTask(id);
        save();
        return super.findTaskById(id);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return super.findEpicById(epic.getId());
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return super.findSubtaskById(subtask.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(Subtask subtask) {
        super.deleteSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}
