package managers.taskManager;

import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;

public class Managers {
    private Managers() {}
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager  getDefaultFileBackedManager(){
        return new FileBackedTasksManager();
    }
}
