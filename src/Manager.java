import java.util.*;

public class Manager {
    private HashMap<Integer, Task> taskById = new HashMap<>();
    private HashMap<Integer, Epic> epicHash = new HashMap<>();
    private HashMap<Integer, Subtask> subsHash = new HashMap<>();
    SingleTask singleTask;
    Subtask subtasks;
    Epic epic;

    public Task getTaskById(int id) {
        Task task = null;
        if (taskById.containsKey(id)) {
            task = taskById.get(id);
        }
        return task;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : taskById.values()) {
            tasks.add(task);
        }
        return tasks;
    }

    public static class IdGenerator {
        private int nextId = 0;

        public int getNextId() {
            return nextId++;
        }
    }

    IdGenerator idGenerator = new IdGenerator();

    public void addTask(SingleTask.ToCreate singleTaskToCreate) {
        int id = idGenerator.getNextId();

        singleTask = new SingleTask(
                id,
                singleTaskToCreate.getGoal(),
                Status.NEW
        );

        taskById.put(singleTask.getId(), singleTask);
    }

    public void addTaskEpic(Epic.ToCreate newEpic) {
        int id = idGenerator.getNextId();

        epic = new Epic(
                id,
                newEpic.getGoalToEpic(),
                Status.NEW
        );
        epicHash.put(epic.getId(), epic);
        taskById.put(epic.getId(), epic);
    }

    public void addTaskSub(Subtask.ToCreate subtask) {
        int id = idGenerator.getNextId();

        subtasks = new Subtask(
                id,
                subtask.getGoalToSub(),
                Status.NEW,
                epic.getId()
        );
        epic.setSubId(id);
        subsHash.put(subtasks.getId(), subtasks);
        taskById.put(subtasks.getId(), subtasks);
    }

    public ArrayList getEpicsSubs(int id) {
        ArrayList<Subtask> subs = new ArrayList<>();
        for (Subtask e : subsHash.values()) {
           if (id == e.getEpicId()) {
               subs.add(e);
           }
        }

        return subs;
    }

    public void doneSub(int doneId) {
        if (epic.getSubId().contains(doneId)) {
            taskById.get(doneId).setStatus(Status.DONE);
        }
    }

    public void doneEpic() {
        int countSub = epic.getSubId().size();
        int countDone = 0;

        for (int id : epic.getSubId()) {
            if (taskById.get(id).getStatus().equals(Status.DONE)) {
                countDone++;
            }
        }

        for (Task type : taskById.values()) {
            if (type.getType().equals(Type.EPIC) && countSub == countDone) {
                epic.setStatus(Status.DONE);
                break;
            } else epic.setStatus(Status.IN_PROGRESS);
        }

        System.out.println("countSub: " + countSub);
        System.out.println("count: " + countDone);

    }

    public void doneSingleTask(int doneId) {
        if (taskById.containsKey(doneId)) {
            for (Task type : taskById.values()) {
                if (type.getType().equals(Type.SINGLE)) {
                    singleTask.setStatus(Status.DONE);
                }
            }
        }
    }

    public void removeAll() {
        taskById.clear();
        System.out.println("Все задачи удалены");
    }

    public void removeById(int removeId) {
        if (taskById.containsKey(removeId)) {
            if ((!taskById.get(removeId).getType().equals(Type.EPIC))) {
                taskById.remove(removeId);
            } else if (taskById.get(removeId).getType().equals(Type.EPIC)) {
                for (Epic e : epicHash.values()) {
                    if (removeId == e.getId()) {
                        for (int subs : e.getSubId()) {
                            taskById.remove(subs);
                        }
                    }
                }
                taskById.remove(removeId);
            }
        }
    }
}
