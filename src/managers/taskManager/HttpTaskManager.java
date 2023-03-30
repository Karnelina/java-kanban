package managers.taskManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.server.KVTaskClient;
import tasks.Epic;
import tasks.SingleTask;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    protected KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager() {
        gson = Managers.getGson();
        client = new KVTaskClient();
    }

    public KVTaskClient getClient() {
        return client;
    }

    @Override
    public void save() {

        var tasks = gson.toJson(singleHash.values());
        client.save("tasks/task", tasks);

        var epics = gson.toJson(epicHash.values());
        client.save("tasks/epic", epics);

        var subtasks = gson.toJson(subsHash.values());
        client.save("tasks/subtask", subtasks);

        var history = gson.toJson(printHistory());
        client.save("tasks/history", history);

    }

    public void load() {

        var jsonTasks = getClient().load("tasks/task");
        var taskType = new TypeToken<List<SingleTask>>() {
        }.getType();
        List<SingleTask> tasks = gson.fromJson(jsonTasks, taskType);
        for (SingleTask task : tasks) {
            singleHash.put(task.getId(), task);
            taskById.put(task.getId(), task);
        }

        var jsonEpics = getClient().load("tasks/epic");
        var epicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epics = gson.fromJson(jsonEpics, epicType);
        for (Epic task : epics) {
            epicHash.put(task.getId(), task);
            taskById.put(task.getId(), task);
        }

        var jsonSubtasks = getClient().load("tasks/subtask");
        var subtaskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = gson.fromJson(jsonSubtasks, subtaskType);
        for (Subtask task : subtasks) {
            subsHash.put(task.getId(), task);
            taskById.put(task.getId(), task);
        }

        var gsonHistory = getClient().load("tasks/history");
        var historyType = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> history = gson.fromJson(gsonHistory, historyType);
        for (int id : history) {
            inMemoryHistoryManager.addInHistory(id);
        }

    }

}
