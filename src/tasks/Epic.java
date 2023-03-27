package tasks;

import enums.*;

import java.util.*;

public class Epic extends Task {
    private Status status;
    private ArrayList<Subtask> subs;

    public Epic(int id, String goal, String description, Status status) {
        super(id, goal, description);
        this.status = status;
        subs = new ArrayList<>();
    }

    public Epic(String goal, String description) {
        super(goal, description);
    }

    public ArrayList<Subtask> getSubs() {
        return subs;
    }

    public void setSub(Subtask subs) {
        this.subs.add(subs);
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
        return Type.EPIC;
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
                getDescription().equals(task.getDescription());
    }

    @Override
    public int hashCode() {
        return getId();
    }

}
