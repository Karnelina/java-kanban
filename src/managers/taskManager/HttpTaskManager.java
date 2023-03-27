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

        var tasks = gson.toJson(getSingleTasks());
        client.save("tasks/task", tasks);

        var epics = gson.toJson(getEpicTasks());
        client.save("tasks/epic", epics);

        var subtasks = gson.toJson(getSubsTasks());
        client.save("tasks/subtask", subtasks);

        var prioritizedTasks = gson.toJson(getTasksTree());
        client.save("tasks", prioritizedTasks);

        var history = gson.toJson(printHistory());
        client.save("tasks/history", history);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        String tasks = gson.toJson(task);
        client.save("tasks/task/\\d+$", tasks);

        return task;
    }

    public void load() {

        var jsonPrioritizedTasks = getClient().load("tasks");
        var prioritizedTaskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> priorityTasks = gson.fromJson(jsonPrioritizedTasks, prioritizedTaskType);
        getTasksTree().addAll(priorityTasks);


        var gsonHistory = getClient().load("tasks/history");
        var historyType = new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(gsonHistory, historyType);
        printHistory().addAll(history);


        var jsonTasks = getClient().load("tasks/task");
        var taskType = new TypeToken<Map<Integer, SingleTask>>(){}.getType();
        Map<Integer, SingleTask> tasks = gson.fromJson(jsonTasks, taskType);
        getSingleTasks().putAll(tasks);


        var jsonEpics = getClient().load("tasks/epic");
        var epicType = new TypeToken<Map<Integer, Epic>>(){}.getType();
        Map<Integer, Epic> epics = gson.fromJson(jsonEpics, epicType);
        getEpicTasks().putAll(epics);


        var jsonSubtasks = getClient().load("tasks/subtask");
        var subtaskType = new TypeToken<Map<Integer, Subtask>>(){}.getType();
        Map<Integer, Subtask> subtasks = gson.fromJson(jsonSubtasks, subtaskType);
        getSubsTasks().putAll(subtasks);

    }

}
