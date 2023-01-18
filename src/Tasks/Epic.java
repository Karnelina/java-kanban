package Tasks;
import Enums.*;
import java.util.*;

public class Epic extends Task {
    private Status status;
    private final ArrayList<Integer> subId;

    public Epic(int id, String goal, Status status) {
        super(id, goal);
        this.status = status;
        subId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId.add(subId);
    }

    public static class ToCreate {
        private final String goal;

        public ToCreate(String goal) {
            this.goal = goal;
        }

        public String getGoalToEpic() {
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
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                " goal = " + getGoal() +
                ", id = " + getId() +
                ", status = '" + status + '\'' +
                '}' + '\n';
    }

}
