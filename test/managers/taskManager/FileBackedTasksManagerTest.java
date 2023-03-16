package managers.taskManager;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tasks.Epic;
import tasks.SingleTask;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTasksManagerTest extends TaskManagerTest <FileBackedTasksManager> {
    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager();
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

        singleSameSub1 = new SingleTask.ToCreate(
                "SingleTask1",
                "Make single",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 2, 2, 0, 0)
        );
    }

    @Test
    public void saveAndLoadTaskTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        Task taskSingle = taskManager.getTaskById(0);
        Task taskSub = taskManager.getTaskById(1);

        taskManager.loadFromFile(FileBackedTasksManager.filePath);
        Task taskFromFile = taskManager.getTaskById(0);
        Task taskFromFile1 = taskManager.getTaskById(1);

        assertEquals(taskSingle, taskFromFile, "Неправильно выгрузились данные");
        assertEquals(taskSub, taskFromFile1, "Неправильно выгрузились данные");
    }

    @Test
    public void emptySaveAndLoadTaskTest() {

        taskManager.loadFromFile(FileBackedTasksManager.filePath);
        Task taskFromFile = taskManager.getTaskById(0);

        assertEquals(null, taskFromFile, "Неправильно выгрузились данные");
    }

}
