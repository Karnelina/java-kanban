package tasks;
import enums.*;

import java.time.Duration;
import java.time.LocalDateTime;


public abstract class Task {
    private int id;
    private final String goal;
    private final String description;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    protected Task(int id, String goal, String description, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.goal = goal;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.finishTime = getFinishTime();
    }

    protected Task(int id, String goal, String description) {
        this.id = id;
        this.goal = goal;
        this.description = description;
    }

    public Task(String goal, String description, Duration duration, LocalDateTime startTime) {
        this.goal = goal;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String goal, String description) {
        this.goal = goal;
        this.description = description;
    }

    public Integer getId() {

        return id;
    }

    public String getGoal() {

        return goal;
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public abstract Status getStatus();
    public abstract void setStatus(Status status);
    public abstract Type getType();
    public abstract String toString();
    public abstract boolean equals(Object obj);
    public abstract int hashCode();

}
