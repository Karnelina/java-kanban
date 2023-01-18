package Tasks;
import Enums.*;
public class SingleTask extends Task {
    private Status status;

    public SingleTask(int id, String goal, Status status) {
        super(id, goal);
        this.status = status;
    }

    public static class ToCreate {
        private final String goal;

        public ToCreate(String goal) {
            this.goal = goal;
        }

        public String getGoal() {
            return goal;
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
        return "SingleTask{" +
                " goal = " + getGoal() +
                ", id = " + getId() +
                ", status = '" + status + '\'' +
                '}' + '\n';
    }


}
