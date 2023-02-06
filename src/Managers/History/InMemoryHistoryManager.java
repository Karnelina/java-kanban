package Managers.History;
import Tasks.*;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList historyList;
    private final Map<Integer, Node<Task>> history;

    public InMemoryHistoryManager() {
        historyList = new CustomLinkedList();
        history = new HashMap<>();
    }

    @Override
    public void addInHistory(Task task) {
        Node<Task> node = historyList.linkLast(task);

        if (history.containsKey(task.getId())) {
            historyList.removeNode(history.get(task.getId()));
        }

        history.put(task.getId(), node);
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            historyList.removeNode(history.get(id));
            history.remove(id);
        }
    }

    @Override
    public void clear() {
        history.clear();
        historyList.clear();
    }
}
