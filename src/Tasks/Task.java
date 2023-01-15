package Tasks;
import Enums.*;


public abstract class Task {
    private int id;
    private String goal;

    protected Task(int id, String goal) {
        this.id = id;
        this.goal = goal;
    }

    public int getId() {

        return id;
    }

    public String getGoal() {

        return goal;
    }

    public void setGoal(String goal) {
        if (goal != null) {
            this.goal = goal;
        }
    }

    public abstract Status getStatus();
    public abstract void setStatus(Status status);
    public abstract Type getType();
    public abstract String toString();



}
