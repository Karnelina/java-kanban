import managers.taskManager.FileBackedTasksManager;
import managers.taskManager.Managers;
import managers.taskManager.TaskManager;
import tasks.*;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefaultFileBackedManager();
        final Path filePath = Path.of("resources/TaskHistory.csv");

        SingleTask.ToCreate singleTaskToCreate = new SingleTask.ToCreate(
                "SingleTask1",
                "Make single",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 1, 3, 4, 0)
        );

        manager.addTask(singleTaskToCreate);

        manager.doneSingleTask(0);

        Epic.ToCreate epic = new Epic.ToCreate("New Epic",
                "Make epic"
        );

        Subtask.ToCreate subtask1 = new Subtask.ToCreate("New sub 1",
                "Make sub 1",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 2, 2, 0, 0),
                epic
        );

        Subtask.ToCreate subtask2 = new Subtask.ToCreate("New sub 2",
                "Make sub 2",
                Duration.ofMinutes(30L),
                LocalDateTime.of(2023, 2, 2, 1, 0),
                epic
        );

        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);
        manager.addTaskSub(subtask2);

        manager.doneSub(2); // Завершение саба

        System.out.println(manager.getTaskById(1)); //Получение любого таска по айди
        System.out.println(manager.getTaskById(3));
        System.out.println(manager.getTaskById(0));

        Epic.ToCreate epic1 = new Epic.ToCreate("New Epic1",
                "Make epic 1"
        );

        Subtask.ToCreate subtask3 = new Subtask.ToCreate("New sub1",
                "Make sub 1",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 3, 1, 0, 0),
                epic1
        );

        manager.addTaskEpic(epic1);
        manager.addTaskSub(subtask3);

        manager.doneSub(5);

        System.out.println(manager.getTaskById(4)); //Получение любого таска по айди

        System.out.println(manager.getTasksTree());

        FileBackedTasksManager.loadFromFile(filePath); //Вытаскивание данных из файла
        System.out.println(manager.getAllTasks()); // Проверка
        manager.printHistory();


    }
}

