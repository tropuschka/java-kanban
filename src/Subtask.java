import java.util.Objects;

public class Subtask extends Task {
    private Integer subId;
    private Integer epicId;

    public Subtask(Integer id, String name, String details, Integer epicId, Integer subId) {
        super(id, name, details);
        this.epicId = epicId;
        this.subId = subId;
    }

    @Override
    public String toString() {
        return "    " + getSubId() + ") " + getName() + "\nАбсолютный id: " + getId() + "\n        Статус: " + getStatus() + "\n        " +
                getDetails();
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

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }
}
