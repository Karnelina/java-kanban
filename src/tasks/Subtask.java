package tasks;
import enums.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Status status;
    private int epicId;
    private Epic parentTask;

    public Subtask(int id, String goal, String description, Status status, Duration duration, LocalDateTime startTime, int epicId) {
        super(id, goal, description, duration, startTime);
        this.status = status;
        this.epicId = epicId;
    }

    public Subtask(String goal, String description, Duration duration, LocalDateTime startTime, Epic parentTask) {
        super(goal, description, duration, startTime);
        this.parentTask = parentTask;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public LocalDateTime getFinishTime() {
        return getStartTime().plus(getDuration());
    }

    @Override
    public String toString() {
        return getId() + ","
                + getType() + ","
                + getGoal() + ","
                + getStatus() + ","
                + getDescription()  + ","
                + getDuration() + ","
                + getStartTime() + ","
                + getFinishTime() + ","
                + getEpicId();
    }

    @Override
    public boolean equals(Object obj) {
        Task task = (Task) obj;
        if (obj == null && (task.getType().equals(Type.EPIC) || task.getType().equals(Type.SINGLE))) {
            return false;
        }
        return getId() == task.getId() &&
                getType() == task.getType() &&
                getGoal().equals(task.getGoal()) &&
                getStatus() == task.getStatus() &&
                getDescription().equals(task.getDescription()) &&
                getDuration().equals(task.getDuration()) &&
                getStartTime().equals(task.getStartTime()) &&
                getFinishTime().equals(task.getFinishTime()) &&
                getEpicId() ==  this.getEpicId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

}
