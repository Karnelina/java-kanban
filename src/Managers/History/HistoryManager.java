package Managers.History;

import Tasks.*;

import java.util.List;

public interface HistoryManager {
    void addInHistory(Task task);
    void remove(int id);
    List<Task> getHistory();
    void clear();
}