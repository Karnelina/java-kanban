package managers.taskManager;

import enums.Status;
import managers.exception.IntersectionException;
import org.junit.jupiter.api.Test;
import tasks.SingleTask;
import tasks.*;

import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected SingleTask.ToCreate singleTaskToCreate;
    protected Epic.ToCreate epic;
    protected Subtask.ToCreate subtask1;
    protected Subtask.ToCreate subtask2;
    protected SingleTask.ToCreate singleSameSub1;

    @Test
    void addTaskTest() {
        taskManager.addTask(singleTaskToCreate);

        final Task savedTask = taskManager.getTaskById(0);

        assertNotNull(savedTask, "Задача не найдена.");

        final List<Task> tasks = new ArrayList<>(taskManager.getAllTasks());

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(savedTask, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addTaskEpicTest() {
        taskManager.addTaskEpic(epic);

        final Task savedTask = taskManager.getTaskById(0);

        assertNotNull(savedTask, "Задача не найдена.");

        final List<Task> tasks = new ArrayList<>(taskManager.getAllTasks());

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(savedTask, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addTaskSub() {
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);

        final Task savedTask = taskManager.getTaskById(1);

        assertNotNull(savedTask, "Задача не найдена.");

        final List<Task> tasks = new ArrayList<>(taskManager.getAllTasks());

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(savedTask, tasks.get(1), "Задачи не совпадают.");
    }

    // Проверка эпика

    @Test
    public void epicIsEmptyTest() {
        taskManager.addTaskEpic(epic);
        Task taskEpic = taskManager.getTaskById(0);

        assertNotNull(taskEpic, "Задача не найдена.");
        assertEquals(Status.NEW, taskEpic.getStatus());
    }

    @Test
    public void epicAllTaskIsNEWTest() {
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);
        Task taskEpic = taskManager.getTaskById(0);

        assertNotNull(taskEpic, "Задача не найдена.");
        assertEquals(Status.NEW, taskEpic.getStatus());
    }

    @Test
    public void epicAllTaskIsDONETest() {
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);
        taskManager.doneSub(1);
        taskManager.doneSub(2);
        Task taskEpic = taskManager.getTaskById(0);

        assertNotNull(taskEpic, "Задача не найдена.");
        assertEquals(Status.DONE, taskEpic.getStatus());
    }

    @Test
    public void epicAllTaskIsInProgressTest() {
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);
        taskManager.doneSub(1);
        Task taskEpic = taskManager.getTaskById(0);

        assertNotNull(taskEpic, "Задача не найдена.");
        assertEquals(Status.IN_PROGRESS, taskEpic.getStatus());
    }

    @Test
    public void getTaskByIdTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);

        Task taskEpic = taskManager.getTaskById(1);
        Task taskSingle = taskManager.getTaskById(0);
        Task taskSub = taskManager.getTaskById(2);

        assertNotNull(taskSingle, "Задача не найдена.");
        assertNotNull(taskEpic, "Задача не найдена.");
        assertNotNull(taskSub, "Задача не найдена.");
        assertNull(taskManager.getTaskById(4), "Задача есть");

        final List<Task> tasks = new ArrayList<>(taskManager.getAllTasks());

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(taskEpic, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    public void getAllTasksTest() {
        taskManager.addTask(singleTaskToCreate);
        Task taskSingle = taskManager.getTaskById(0);

        assertNotNull(taskSingle, "Задача не найдена.");

        final List<Task> tasks = new ArrayList<>(taskManager.getAllTasks());

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(taskSingle, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void getEpicsSubsTest() {
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);

        List<Subtask> sub = taskManager.getEpicsSubs(0);

        assertNotNull(sub.get(0), "Задачи не возвращаются.");
        assertEquals(1, sub.size(), "Неверное количество задач.");
        assertEquals(1, sub.size(), "Неверное количество задач.");
    }

    @Test
    public void newAndDoneSubTest() {
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTask(singleTaskToCreate);
        Task taskSub = taskManager.getTaskById(1);

        assertEquals(Status.NEW, taskSub.getStatus(), "Другой статус");

        taskManager.doneSub(1);
        taskManager.doneSub(2);
        Task taskSub1 = taskManager.getTaskById(1);
        Task taskSingle = taskManager.getTaskById(2);


        assertEquals(Status.DONE, taskSub1.getStatus(), "Другой статус");
        assertEquals(Status.NEW, taskSingle.getStatus(), "Другой статус");
    }

    @Test
    public void newAndDoneSingleTaskTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        Task taskSingle = taskManager.getTaskById(0);

        assertEquals(Status.NEW, taskSingle.getStatus(), "Другой статус");

        taskManager.doneSingleTask(0);
        taskManager.doneSingleTask(1);
        Task taskSingle1 = taskManager.getTaskById(0);
        Task taskEpic = taskManager.getTaskById(1);

        assertEquals(Status.DONE, taskSingle1.getStatus(), "Другой статус");
        assertEquals(Status.NEW, taskEpic.getStatus(), "Другой статус");
    }

    @Test
    public void updateTimeOfEpic() {
        taskManager.addTaskEpic(epic);
        Task taskEpic = taskManager.getTaskById(0);

        assertNull(taskEpic.getStartTime());
        assertNull(taskEpic.getFinishTime());
        assertNull(taskEpic.getDuration());

        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);

        Task taskSub = taskManager.getTaskById(1);
        Task taskSub1 = taskManager.getTaskById(2);
        Duration duration = Duration.ZERO;
        duration = duration.plus(taskSub.getDuration()).plus(taskSub1.getDuration());

        assertEquals(taskSub.getStartTime(), taskEpic.getStartTime(), "Другое время");
        assertEquals(taskSub1.getFinishTime(), taskEpic.getFinishTime(), "Другое время");
        assertEquals(duration, taskEpic.getDuration(), "Другое время");
    }

    @Test
    public void getPrioritizedTasks() {
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);
        taskManager.addTask(singleSameSub1);
        Task single = taskManager.getTaskById(3);
        Task sub = taskManager.getTaskById(1);

        final IntersectionException exception = assertThrows(
                IntersectionException.class,

                () -> taskManager.getTasksTree());

        assertEquals("Найдено пересечение между " + single.getId() + " и " + sub.getId(),
                exception.getMessage());
    }

    @Test
    public void getTasksTreeTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);
        Task taskSingle = taskManager.getTaskById(0);
        Task taskSub = taskManager.getTaskById(2);
        Task taskSub1 = taskManager.getTaskById(3);

        assertNotNull(taskSingle, "Задача не найдена.");
        assertNotNull(taskSub, "Задача не найдена.");
        assertNotNull(taskSub1, "Задача не найдена.");

        final List<Task> tasks = new ArrayList<>(taskManager.getTasksTree());

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(taskSingle, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void removeAllTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);

        assertEquals(4, taskManager.getAllTasks().size(), "Неверное количество задач.");

        taskManager.removeAll();
        int i = taskManager.getAllTasks().size();

        assertEquals(0, i, "Неверное количество задач.");

    }

    @Test
    public void removeAllTreeTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);

        assertEquals(3, taskManager.getTasksTree().size(), "Неверное количество задач.");

        taskManager.removeAll();
        int i = taskManager.getTasksTree().size();

        assertEquals(0, i, "Неверное количество задач.");

    }

    @Test
    public void removeByIdTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);

        assertEquals(4, taskManager.getAllTasks().size(), "Неверное количество задач.");

        taskManager.removeById(0);

        assertEquals(3, taskManager.getAllTasks().size(), "Неверное количество задач.");

        taskManager.removeById(5);

        assertEquals(3, taskManager.getAllTasks().size(), "Неверное количество задач.");

        taskManager.removeById(1);

        assertEquals(0, taskManager.getAllTasks().size(), "Неверное количество задач.");
    }

    @Test
    public void removeByIdWithTreeTest() {
        taskManager.addTask(singleTaskToCreate);
        taskManager.addTaskEpic(epic);
        taskManager.addTaskSub(subtask1);
        taskManager.addTaskSub(subtask2);

        assertEquals(3, taskManager.getTasksTree().size(), "Неверное количество задач.");

        taskManager.removeById(0);

        assertEquals(2, taskManager.getTasksTree().size(), "Неверное количество задач.");

        taskManager.removeById(5);

        assertEquals(2, taskManager.getTasksTree().size(), "Неверное количество задач.");

        taskManager.removeById(1);

        assertEquals(0, taskManager.getTasksTree().size(), "Неверное количество задач.");
    }

}
