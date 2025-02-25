package managing;

import taskmodels.*;

public class TaskFromString {
    public static Task fromString(String taskString) {
        String[] taskList = taskString.split(",");
        Task task;
        TaskType type = TaskType.valueOf(taskList[1]);
        Integer id = Integer.parseInt(taskList[0]);
        String name = taskList[2];
        String descr = taskList[4];
        TaskStatus status = TaskStatus.valueOf(taskList[3]);
        if (type.equals(TaskType.EPIC)) {
            task = new Epic(id, name, descr);
        } else if (type.equals(TaskType.SUBTASK)) {
            Integer epicId = Integer.parseInt(taskList[5]);
            task = new Subtask(id, name, descr, epicId);
        } else {
            task = new Task(id, name, descr);
        }
        task.setStatus(status);
        return task;
    }
}
