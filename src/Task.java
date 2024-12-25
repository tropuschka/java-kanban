import java.util.Objects;

public class Task {
    private String name;
    private String details;
    private Integer id;
    private TaskStatus status;

    public Task(Integer id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details;
        status = TaskStatus.NEW;
    }

    @Override
    public String toString() {
        return getId() + ". " + getName() + "\nСтатус: " + getStatus() + "\n" + getDetails();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(details, task.details) &&
                Objects.equals(id, task.id) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, details, id, status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
