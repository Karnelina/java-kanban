package managers.taskManager;
import tasks.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {
    List<Task> getAllTasks();
    Task getTaskById(int id);
    void addTask(SingleTask singleTaskToCreate);
    void addTaskEpic(Epic newEpic);
    void addTaskSub(Subtask subtask);
    Map<Integer, SingleTask> getSingleTasks();
    Map<Integer, Subtask> getSubsTasks();
    Map<Integer, Epic> getEpicTasks();
    List<Subtask> getEpicsSubs(int id);
    void doneSub(int doneId);
    void doneSingleTask(int doneId);
    void removeAll();
    void removeById(int removeId);
    List<Task> printHistory();
    Set<Task> getTasksTree();

}
