package Managers.TaskManager;
import Tasks.*;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();
    Task getTaskById(int id);
    void addTask(SingleTask.ToCreate singleTaskToCreate);
    void addTaskEpic(Epic.ToCreate newEpic);
    void addTaskSub(Subtask.ToCreate subtask);
    List<Subtask> getEpicsSubs(int id);
    void doneSub(int doneId);
    void doneEpic();
    void doneSingleTask(int doneId);
    void removeAll();
    void removeById(int removeId);
    void printHistory();
}
