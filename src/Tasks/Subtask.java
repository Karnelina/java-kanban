package Tasks;
import Enums.*;

public class Subtask extends Task {
    private Status status;
    private final int epicId;

    public Subtask(int id, String goal, String description, Status status, int epicId) {
        super(id, goal, description);
        this.status = status;
        this.epicId = epicId;
    }

    public static class ToCreate {
        private final String goal;
        private final String description;
        private final Epic.ToCreate parrentTask;

        public ToCreate(String goal, String description, Epic.ToCreate parrentTask) {
            this.goal = goal;
            this.parrentTask = parrentTask;
            this.description = description;
        }

        public String getGoalToSub() {
            return goal;
        }
        public String getDescription() {
            return description;
        }
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
    public String toString() {
        return getId() + ","
                + getType() + ","
                + getGoal() + ","
                + getStatus() + ","
                + getDescription() + ","
                + getEpicId();
    }

}
