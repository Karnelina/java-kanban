package DifferentManagers;
import Tasks.*;
import java.util.*;
public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> history = new ArrayList<>();
    private static final int LIMIT = 10;

    @Override
    public void addInHistory(Task task){
        if (history.size() >= LIMIT) {
            history.remove(0);
            history.add(task);
        } else {
            history.add(task);
        }

    }

    @Override
    public List<Task> getHistory() {
        return   new ArrayList<>(history);
    }



}
