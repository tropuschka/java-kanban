package taskmodels;

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
        subtasks.remove(subtaskId);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return getId() + ". " + getName() + " (эпик)\nСтатус: " + getStatus() + "\n" + getDetails();
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
