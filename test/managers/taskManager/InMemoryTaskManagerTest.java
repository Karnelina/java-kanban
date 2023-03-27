package managers.taskManager;

import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.SingleTask;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

public class InMemoryTaskManagerTest extends TaskManagerTest <InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        singleTaskToCreate = new SingleTask(
                "SingleTask1",
                "Make single",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 1, 3, 0, 0)
        );

        epic = new Epic("New Epic",
                "Make epic"
        );

        subtask1 = new Subtask("New sub 1",
                "Make sub 1",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 2, 2, 0, 0),
                epic
        );

        subtask2 = new Subtask("New sub 2",
                "Make sub 2",
                Duration.ofMinutes(30L),
                LocalDateTime.of(2023, 2, 2, 1, 0),
                epic
        );

        singleSameSub1 = new SingleTask(
                "SingleTask1",
                "Make single",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 2, 2, 0, 0)
        );
    }

}
