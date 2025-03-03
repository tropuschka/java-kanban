package managing;

import taskmodels.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskFromString {
    public static Task fromString(String taskString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        String[] taskList = taskString.split(",");
        Task task;
        TaskType type = TaskType.valueOf(taskList[1]);
        Integer id = Integer.parseInt(taskList[0]);
        String name = taskList[2];
        String descr = taskList[4];
        TaskStatus status = TaskStatus.valueOf(taskList[3]);
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        Duration duration = null;
        if (!taskList[5].equals("-")) {
            startDate = LocalDateTime.parse(taskList[5], formatter);
            endDate = LocalDateTime.parse(taskList[6], formatter);
            duration = Duration.between(startDate, endDate);
        }
        if (type.equals(TaskType.EPIC)) {
            task = new Epic(id, name, descr);
        } else if (type.equals(TaskType.SUBTASK)) {
            Integer epicId = Integer.parseInt(taskList[7]);
            if (startDate != null) task = new Subtask(id, name, descr, epicId, startDate.format(formatter),
                    duration.toString());
            else task = new Subtask(id, name, descr, epicId);
        } else {
            if (startDate != null) task = new Task(id, name, descr, startDate.format(formatter), duration.toString());
            else task = new Task(id, name, descr);
        }
        task.setStatus(status);
        return task;
    }
}
