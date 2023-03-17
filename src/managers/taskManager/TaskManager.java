package managers.taskManager;
import tasks.*;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getAllTasks();
    Task getTaskById(int id);
    void addTask(SingleTask.ToCreate singleTaskToCreate);
    void addTaskEpic(Epic.ToCreate newEpic);
    void addTaskSub(Subtask.ToCreate subtask);
    List<Subtask> getEpicsSubs(int id);
    void doneSub(int doneId);
    void doneSingleTask(int doneId);
    void removeAll();
    void removeById(int removeId);
    List<Task> printHistory();
    Set<Task> getTasksTree();
}
