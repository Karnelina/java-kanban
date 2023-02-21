package Tasks;
import Enums.*;
import java.util.*;

public class Epic extends Task {
    private Status status;
    private final ArrayList<Integer> subId;

    public Epic(int id, String goal, String description, Status status) {
        super(id, goal, description);
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
        private final String description;

        public ToCreate(String goal, String description) {
            this.goal = goal;
            this.description = description;
        }

        public String getGoalToEpic() {
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
        return Type.EPIC;
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
