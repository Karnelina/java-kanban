package managers;

import Tasks.*;

import java.util.List;

public interface HistoryManager {
    void addInHistory(Task task);

    List<Task> getHistory();

}
