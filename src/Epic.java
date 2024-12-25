import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();
    private int subtaskAmount = 0;

    public Epic(Integer id, String name, String details) {
        super(id, name, details);
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
        return subtaskAmount == epic.subtaskAmount && Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks, subtaskAmount);
    }

    public String getWithSubs() {
        String result = toString() + "\nПодзадачи:";
        for (Integer subtask : subtasks) {
            result = result + "\n" + subtask.toString();
        }
        return result;
    }

    public void increaseSubsAmount() {
        subtaskAmount++;
    }

    public void decreaseSubsAmount() {
        subtaskAmount--;
    }

    public void addSubtask(Integer subtask) {
        increaseSubsAmount();
        subtasks.add(subtask);
    }

    public void clearAllSubs() {
        subtaskAmount = 0;
        subtasks.clear();
    }

    public void deleteSubtask(int subtaskId) {
        decreaseSubsAmount();
        subtasks.remove(subtaskId);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }
}
