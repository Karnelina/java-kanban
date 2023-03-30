package managers.history;

import tasks.*;

import java.util.List;

public interface HistoryManager {
    void addInHistory(int id);
    void remove(int id);
    List<Integer> getHistory();
    void clear();
}
