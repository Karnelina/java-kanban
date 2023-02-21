package Managers.TaskManager;

import Managers.Managers;
import Tasks.*;
import Enums.*;
import Managers.History.HistoryManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskById = new HashMap<>();
    private final HashMap<Integer, Epic> epicHash = new HashMap<>();
    private final HashMap<Integer, Subtask> subsHash = new HashMap<>();
    SingleTask singleTask;
    Subtask subtasks;
    Epic epic;
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    @Override
    public Task getTaskById(int id) {
        Task task = null;
        if (taskById.containsKey(id)) {
            task = taskById.get(id);
            inMemoryHistoryManager.addInHistory(task);
        }
        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
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

    @Override
    public void addTask(SingleTask.ToCreate singleTaskToCreate) {
        int id = idGenerator.getNextId();

        singleTask = new SingleTask(
                id,
                singleTaskToCreate.getGoal(),
                singleTaskToCreate.getDescription(),
                Status.NEW
        );

        taskById.put(singleTask.getId(), singleTask);
    }

    @Override
    public void addTaskEpic(Epic.ToCreate newEpic) {
        int id = idGenerator.getNextId();

        epic = new Epic(
                id,
                newEpic.getGoalToEpic(),
                newEpic.getDescription(),
                Status.NEW
        );
        epicHash.put(epic.getId(), epic);
        taskById.put(epic.getId(), epic);
    }

    @Override
    public void addTaskSub(Subtask.ToCreate subtask) {
        int id = idGenerator.getNextId();

        subtasks = new Subtask(
                id,
                subtask.getGoalToSub(),
                subtask.getDescription(),
                Status.NEW,
                epic.getId()
        );
        epic.setSubId(id);
        subsHash.put(subtasks.getId(), subtasks);
        taskById.put(subtasks.getId(), subtasks);
    }

    @Override
    public List<Subtask> getEpicsSubs(int id) {
        List<Subtask> subs = new ArrayList<>();
        for (Subtask e : subsHash.values()) {
            if (id == e.getEpicId()) {
                subs.add(e);
            }
        }

        return subs;
    }

    @Override
    public void doneSub(int doneId) {
        if (epic.getSubId().contains(doneId)) {
            taskById.get(doneId).setStatus(Status.DONE);
        }
    }

    @Override
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

    @Override
    public void doneSingleTask(int doneId) {
        if (taskById.containsKey(doneId)) {
            for (Task type : taskById.values()) {
                if (type.getType().equals(Type.SINGLE)) {
                    singleTask.setStatus(Status.DONE);
                }
            }
        }
    }

    @Override
    public void removeAll() {
        taskById.clear();
        inMemoryHistoryManager.clear();
        System.out.println("Все задачи удалены");
    }

    private void removeSinglesAndSub(int removeId) {
        if (taskById.containsKey(removeId) && (!taskById.get(removeId).getType().equals(Type.EPIC))) {
            taskById.remove(removeId);
        }
    }

    private void removeEpic(int removeId) {
        if (taskById.containsKey(removeId)) {
            for (Epic e : epicHash.values()) {
                if (removeId == e.getId()) {
                    for (int subs : e.getSubId()) {
                        taskById.remove(subs);
                        inMemoryHistoryManager.remove(subs);
                    }
                }
            }
            taskById.remove(removeId);
            inMemoryHistoryManager.remove(removeId);
        }
    }

    @Override
    public void removeById(int removeId) {
        if ((!taskById.get(removeId).getType().equals(Type.EPIC))) {
            removeSinglesAndSub(removeId);
            inMemoryHistoryManager.remove(removeId);
        } else if (taskById.get(removeId).getType().equals(Type.EPIC)) {
            removeEpic(removeId);
        }
    }

    @Override
    public void printHistory() {
        System.out.println("История: ");
        System.out.println(inMemoryHistoryManager.getHistory());
    }

}
