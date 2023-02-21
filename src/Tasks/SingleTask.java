package Tasks;
import Enums.*;
public class SingleTask extends Task {
    private Status status;

    public SingleTask(int id, String goal, String description, Status status) {
        super(id, goal, description);
        this.status = status;
    }

    public static class ToCreate {
        private final String goal;
        private final String description;

        public ToCreate(String goal, String description) {
            this.goal = goal;
            this.description = description;
        }

        public String getGoal() {
            return goal;
        }
        public String getDescription() {
            return description;
        }

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
    public String toString() {
        return getId() + ","
                + getType() + ","
                + getGoal() + ","
                + getStatus() + ","
                + getDescription();
    }


}
