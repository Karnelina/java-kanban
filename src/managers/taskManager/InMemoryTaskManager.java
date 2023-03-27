package managers.taskManager;

import managers.exception.IntersectionException;
import tasks.*;
import enums.*;
import managers.history.HistoryManager;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> taskById = new HashMap<>();
    protected final HashMap<Integer, Epic> epicHash = new HashMap<>();
    protected final HashMap<Integer, Subtask> subsHash = new HashMap<>();
    protected final HashMap<Integer, SingleTask> singleHash = new HashMap<>();
    protected Set<Task> tasksTree;
    SingleTask singleTask;
    Subtask subtasks;
    Epic epic;
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    int id;

    protected int getNextId() {

        return ++id;
    }

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
        List<Task> tasks = new ArrayList<>(taskById.values());
        return tasks;
    }

    @Override
    public void addTask(SingleTask singleTaskToCreate) {

        singleTask = new SingleTask(
                id,
                singleTaskToCreate.getGoal(),
                singleTaskToCreate.getDescription(),
                Status.NEW,
                singleTaskToCreate.getDuration(),
                singleTaskToCreate.getStartTime()
        );

        singleHash.put(singleTask.getId(), singleTask);
        taskById.put(singleTask.getId(), singleTask);
    }

    @Override
    public void addTaskEpic(Epic newEpic) {

        epic = new Epic(
                id,
                newEpic.getGoal(),
                newEpic.getDescription(),
                Status.NEW
        );
        epicHash.put(epic.getId(), epic);
        taskById.put(epic.getId(), epic);
    }

    @Override
    public void addTaskSub(Subtask subtask) {

        subtasks = new Subtask(
                id,
                subtask.getGoal(),
                subtask.getDescription(),
                Status.NEW,
                subtask.getDuration(),
                subtask.getStartTime(),
                epic.getId()

        );
        epic.setSub(subtasks);
        subsHash.put(subtasks.getId(), subtasks);
        taskById.put(subtasks.getId(), subtasks);
        updateTimeOfEpic();
    }

    @Override
    public List<Subtask> getEpicsSubs(int id) {
        List<Subtask> subs = new ArrayList<>();
        if (epicHash.containsKey(id)) {
            for (Subtask e : subsHash.values()) {
                if (id == e.getEpicId()) {
                    subs.add(e);
                }
            }
        }
        return subs;
    }

    @Override
    public Map<Integer, Epic> getEpicTasks() {
        return epicHash;
    }

    @Override
    public Map<Integer, Subtask> getSubsTasks() {
        return subsHash;
    }

    @Override
    public Map<Integer, SingleTask> getSingleTasks() {
        return singleHash;
    }

    @Override
    public void doneSub(int doneId) {
        if (subsHash.containsKey(doneId)) {
            taskById.get(doneId).setStatus(Status.DONE);
        }
        doneEpic();
    }

    private void doneEpic() {
        int countSub = epic.getSubs().size();
        int countDone = 0;
        int countNew = 0;

        for (Subtask id : epic.getSubs()) {
            if (taskById.get(id.getId()).getStatus().equals(Status.DONE)) {
                countDone++;
            }
        }

        for (Subtask id : epic.getSubs()) {
            if (taskById.get(id.getId()).getStatus().equals(Status.NEW)) {
                countNew++;
            }
        }

        for (Task type : taskById.values()) {
            if (type.getType().equals(Type.EPIC) && countSub == countDone) {
                epic.setStatus(Status.DONE);
                break;

            }
            if (type.getType().equals(Type.EPIC) && countSub == countNew) {
                epic.setStatus(Status.NEW);
                break;

            } else epic.setStatus(Status.IN_PROGRESS);
        }
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

     public void updateTimeOfEpic() {
        List<Task> epicSubs = List.copyOf(epic.getSubs());
        if (epicSubs != null && !epicSubs.isEmpty()) {
            LocalDateTime earlyStartTime = LocalDateTime.MAX;
            LocalDateTime latestEndTime = LocalDateTime.MIN;

            Duration durationSum = Duration.ZERO;
            for (Task sub : epicSubs) {

                if (sub.getDuration() != null) {
                    durationSum = durationSum.plus(sub.getDuration());
                }

                LocalDateTime subStartTime = sub.getStartTime();
                if (subStartTime != null && subStartTime.isBefore(earlyStartTime)) {
                    earlyStartTime = subStartTime;
                }

                LocalDateTime subEndTime = sub.getFinishTime();
                if (subStartTime != null && subEndTime.isAfter(latestEndTime)) {
                    latestEndTime = subEndTime;
                }
            }
            epic.setStartTime(earlyStartTime);
            epic.setDuration(durationSum);
            epic.setFinishTime(latestEndTime);
        }
    }

    private void getPrioritizedTasks() {
        tasksTree = new TreeSet<>((o1, o2) -> {
            if (o1.getStartTime().isAfter(o2.getStartTime())) {
                return 1;
            } else if (o1.getStartTime().isBefore(o2.getStartTime())) {
                return -1;
            } else {
                return 0;
            }
        });

        for (Task task : taskById.values()) {
            if (task.getType().equals(Type.EPIC)) {
                continue;
            }
            checkIntersections(task);
            tasksTree.add(task);
        }
    }

    private void checkIntersections(Task task) {

        for (Task prioritizedTask : tasksTree) {

            if (prioritizedTask.getFinishTime().isBefore(task.getStartTime()) ||
                    task.getFinishTime().isBefore(prioritizedTask.getStartTime())) {
                continue;
            }

                throw new IntersectionException("Найдено пересечение между "
                        + task.getId()
                        + " и "
                        + prioritizedTask.getId());
        }
    }

    @Override
    public Set<Task> getTasksTree() {
        getPrioritizedTasks();
        return tasksTree;
    }

    @Override
    public void removeAll() {
        taskById.clear();
        inMemoryHistoryManager.clear();
        if (tasksTree != null) {
            tasksTree.clear();
        }
        System.out.println("Все задачи удалены");
    }

    private void removeSinglesAndSub(int removeId) {
        if (taskById.containsKey(removeId) && (!taskById.get(removeId).getType().equals(Type.EPIC))) {
            taskById.remove(removeId);
            if (tasksTree != null) {
                getPrioritizedTasks();
            }
        }
    }

    private void removeEpic(int removeId) {
            for (Epic e : epicHash.values()) {
                if (removeId == e.getId()) {
                    for (Subtask subs : e.getSubs()) {
                        taskById.remove(subs.getId());

                        inMemoryHistoryManager.remove(subs.getId());
                    }
                }
            }
            taskById.remove(removeId);
        if (tasksTree != null) {
            getPrioritizedTasks();
        }
            inMemoryHistoryManager.remove(removeId);
    }

    @Override
    public void removeById(int removeId) {
        if (taskById.containsKey(removeId)) {
            if ((!taskById.get(removeId).getType().equals(Type.EPIC))) {
                removeSinglesAndSub(removeId);
                inMemoryHistoryManager.remove(removeId);
            } else if (taskById.get(removeId).getType().equals(Type.EPIC)) {
                removeEpic(removeId);
            }
        }
    }

    @Override
    public List<Task> printHistory() {
        return inMemoryHistoryManager.getHistory();
    }

}
