package Managers;

import Managers.History.HistoryManager;
import Managers.History.InMemoryHistoryManager;
import Managers.TaskManager.FileBackedTasksManager;
import Managers.TaskManager.TaskManager;
import Managers.TaskManager.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getBackFileManager(){
        return new FileBackedTasksManager();
    }
}
