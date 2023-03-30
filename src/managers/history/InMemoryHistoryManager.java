package managers.history;
import tasks.*;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList historyList;
    protected final List<Node<Integer>> history;

    public InMemoryHistoryManager() {
        historyList = new CustomLinkedList();
        history = new ArrayList<Node<Integer>>();
    }

    @Override
    public void addInHistory(int id) {
        Node<Integer> node = historyList.linkLast(id);

        if (history.contains(id)) {
            historyList.removeNode(history.get(id));
        }

        history.add(node);
    }

    @Override
    public List<Integer> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(int id) {
        if (history.contains(id)) {
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
