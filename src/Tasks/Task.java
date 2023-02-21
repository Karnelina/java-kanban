package Tasks;
import Enums.*;


public abstract class Task {
    private final int id;
    private final String goal;
    private final String description;

    protected Task(int id, String goal, String description) {
        this.id = id;
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

    public abstract Status getStatus();
    public abstract void setStatus(Status status);
    public abstract Type getType();
    public abstract String toString();
}
