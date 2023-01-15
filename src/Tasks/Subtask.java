package Tasks;
import Enums.*;
import java.util.*;

public class Subtask extends Task {
    private Status status;
    private int epicId;

    public Subtask(int id, String goal, Status status, int epicId) {
        super(id, goal);
        this.status = status;
        this.epicId = epicId;
    }

    public static class ToCreate {
        private final String goal;
        private final Epic.ToCreate parrentTask;

        public ToCreate(String goal, Epic.ToCreate parrentTask) {
            this.goal = goal;
            this.parrentTask = parrentTask;
        }

        public String getGoalToSub() {
            return goal;
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
        return Type.SUB;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                " goal = '" + getGoal() +
                ", id = " + getId() +
                ", status = '" + status + '\'' +
                ", EpicID = " + epicId +
                '}' + '\n';
    }

}
