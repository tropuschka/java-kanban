package taskmodels;

import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer id, String name, String details, Integer epicId) {
        super(id, name, details);
        this.epicId = epicId;
        super.setType(TaskType.SUBTASK);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public Subtask createHistoryExample() {
        Subtask newTask = new Subtask(super.getId(), super.getName(), super.getDetails(), epicId);
        newTask.setStatus(super.getStatus());
        return newTask;
    }

    @Override
    public String toString() {
        return "    " + getId() + ") " + getName() + "\n        Статус: " + getStatus() +
                "\n        " + getDetails();
    }

    @Override
    public String toFile() {
        //id,type,name,status,description,epic
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDetails() + "," + getEpicId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
