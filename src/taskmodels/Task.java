package taskmodels;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private String details;
    private Integer id;
    private TaskStatus status;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(Integer id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details;
        status = TaskStatus.NEW;
    }

    public Task(Integer id, String name, String details, String startTime, String duration) {
        this.id = id;
        this.name = name;
        this.details = details;
        status = TaskStatus.NEW;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        duration = duration.substring(2, duration.length() - 1);
        this.duration = Duration.ofMinutes(Integer.parseInt(duration));
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

    public Task createHistoryExample() {
        Task newTask = new Task(id, name, details);
        newTask.setStatus(status);
        return newTask;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getParsedStartTime() {
        return startTime.format(formatter);
    }

    public String getParsedEndTime() {
        return getEndTime().format(formatter);
    }

    public String getStringDuration() {
        return duration.toString();
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String toFile() {
        //id,type,name,status,description,start date,end date,epic
        String line;
        if (startTime != null) line = getId() + "," + getType() + "," + getName() + "," + getStatus() + ","
                + getDetails() + "," + getStartTime().format(formatter) + "," + getEndTime().format(formatter);
        else line = getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDetails() + ",-,-";
        return line;
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
}
