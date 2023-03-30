package managers.taskManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.server.adapters.*;
import tasks.Epic;
import tasks.Subtask;

import java.time.*;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultFileBackedManager() {
        return new FileBackedTasksManager();
    }

    public static TaskManager getDefaultHttpManager() {
        return new HttpTaskManager();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}
