package server;

import managers.server.KVServer;
import managers.taskManager.HttpTaskManager;
import managers.taskManager.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTasksManagerTest extends TaskManagerTest<HttpTaskManager> {

    protected KVServer server;

    @BeforeEach
    public void loadInitialConditions() throws IOException {
        singleTaskToCreate = new SingleTask(
                "SingleTask1",
                "Make single",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023,1,3,0,0)
        );

        epic = new Epic("New Epic",
                "Make epic"
        );

        subtask1 = new Subtask("New sub 1",
                "Make sub 1",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023,2,2,0,0),
                epic
        );

        subtask2 = new Subtask("New sub 2",
                "Make sub 2",
                Duration.ofMinutes(30L),
                LocalDateTime.of(2023,2,2,1,0),
                epic
        );

        singleSameSub1 = new SingleTask(
                "SingleTask1",
                "Make single",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 2, 2, 0, 0)
        );

        server = new KVServer();
        taskManager = new HttpTaskManager();
        server.start();

    }

    @AfterEach
    void serverStop() {

        server.stop();

    }

    @Test
    void loadFromServerTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);

        taskManager.getTaskById(0);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(3);

        taskManager.load();

        assertEquals(4, taskManager.getAllTasks().size());
        assertEquals(1, taskManager.getSingleTasks().size());
        assertEquals(8, taskManager.printHistory().size());

    }

}
