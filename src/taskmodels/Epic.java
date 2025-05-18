package taskmodels;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(Integer id, String name, String details) {
        super(id, name, details);
    }

    public String getWithSubs() {
        String result = toString() + "\nПодзадачи:";
        for (Integer subtask : subtasks) {
            result = result + "\n" + subtask.toString();
        }
        return result;
    }

    public void addSubtask(Integer subtask) {
        subtasks.add(subtask);
    }

    public void clearAllSubs() {
        subtasks.clear();
    }

    public void deleteSubtask(int subtaskId) {
        subtasks.remove((Integer) subtaskId);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    private void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = new ArrayList<>(subtasks);
    }

    public Epic createHistoryExample() {
        Epic newTask = new Epic(super.getId(), super.getName(), super.getDetails());
        newTask.setStatus(super.getStatus());
        newTask.setSubtasks(subtasks);
        return newTask;
    }

    public void updateStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void addDuration(Duration subtaskDuration) {
        if (this.duration == null) this.duration = subtaskDuration;
        else this.duration = this.duration.plus(subtaskDuration);
    }

    public void decreaseDuration(Duration subtaskDuration) {
        this.duration = this.duration.minus(subtaskDuration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return getId() + ". " + getName() + " (эпик)\nСтатус: " + getStatus() + "\n" + getDetails();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
