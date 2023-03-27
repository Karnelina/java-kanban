package tasks;
import enums.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class SingleTask extends Task {
    private Status status;

    public SingleTask(int id, String goal, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(id, goal, description, duration, startTime);
        this.status = status;
    }

    public SingleTask(String goal, String description, Duration duration, LocalDateTime startTime) {
        super(goal, description, duration, startTime);
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
        return Type.SINGLE;
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
                + getDescription() + ","
                + getDuration() + ","
                + getStartTime() + ","
                + getFinishTime();
    }

    @Override
    public boolean equals(Object obj) {
        Task task = (Task) obj;
        if (obj == null && task.getType().equals(Type.SUBTASK)) {
            return false;
        }
        return getId() == task.getId() &&
                getType() == task.getType() &&
                getGoal().equals(task.getGoal()) &&
                getStatus() == task.getStatus() &&
                getDescription().equals(task.getDescription()) &&
                getDuration().equals(task.getDuration()) &&
                getStartTime().equals(task.getStartTime()) &&
                getFinishTime().equals(task.getFinishTime());
    }

    @Override
    public int hashCode() {
        return getId();
    }

}
