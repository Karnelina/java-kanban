package managers.history;

import managers.taskManager.InMemoryTaskManager;
import managers.taskManager.TaskManager;
import tasks.Epic;
import tasks.SingleTask;
import tasks.Subtask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    TaskManager taskManager;
    InMemoryHistoryManager InMemoryHistoryManager;
    SingleTask.ToCreate singleTaskToCreate;
    Epic.ToCreate epic;
    Subtask.ToCreate subtask1;
    Subtask.ToCreate subtask2;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        InMemoryHistoryManager = new InMemoryHistoryManager();
        singleTaskToCreate = new SingleTask.ToCreate(
                "SingleTask1",
                "Make single",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 1, 3, 0, 0)
        );

        epic = new Epic.ToCreate("New Epic",
                "Make epic"
        );

        subtask1 = new Subtask.ToCreate("New sub 1",
                "Make sub 1",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 2, 2, 0, 0),
                epic
        );

        subtask2 = new Subtask.ToCreate("New sub 2",
                "Make sub 2",
                Duration.ofMinutes(30L),
                LocalDateTime.of(2023, 2, 2, 1, 0),
                epic
        );
    }

    @Test
    public void addInHistoryTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);

        assertEquals(0, InMemoryHistoryManager.getHistory().size(), "Список не пустой");

        taskManager.getTaskById(0);
        assertEquals(1, taskManager.printHistory().size(), "Список пустой");
    }

    @Test
    public void duplicatHistoryTest() {
        assertEquals(0, InMemoryHistoryManager.getHistory().size(), "Список не пустой");

        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);

        assertEquals(0, InMemoryHistoryManager.getHistory().size(), "Список не пустой");

        taskManager.getTaskById(0);
        taskManager.getTaskById(2);
        taskManager.getTaskById(0);
        assertEquals(2, taskManager.printHistory().size(), "Список пустой");
    }
}
