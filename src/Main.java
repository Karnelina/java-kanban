import managers.server.KVServer;
import managers.taskManager.HttpTaskManager;
import managers.taskManager.Managers;
import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    /*
    Проект пока еще не закончен, но так как дедлайн отправлю на первую итерацию в таком виде.
    Почему-то не хочет работать toJson/fromJson надеюсь в ближайшее время найду ошибку
     */

    public static void main(String[] args) throws IOException {

        new KVServer().start();
        var manager = Managers.getDefaultHttpManager();


        SingleTask singleTaskToCreate = new SingleTask(
                "SingleTask1",
                "Make single",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 1, 3, 4, 0)
        );

        manager.addTask(singleTaskToCreate);


        manager.doneSingleTask(0);

        Epic epic = new Epic("New Epic",
                "Make epic"
        );

        Subtask subtask1 = new Subtask("New sub 1",
                "Make sub 1",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 2, 2, 0, 0),
                epic
        );

        /*Subtask subtask2 = new Subtask("New sub 2",
                "Make sub 2",
                Duration.ofMinutes(30L),
                LocalDateTime.of(2023, 2, 2, 1, 0),
                epic
        );*/

        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);
        //manager.addTaskSub(subtask2);

        //manager.doneSub(2); // Завершение саба
        System.out.println(manager.getTaskById(1)); //Получение любого таска по айди
        System.out.println(manager.getTaskById(0));

        /*Epic epic1 = new Epic("New Epic1",
                "Make epic 1"
        );

        Subtask subtask3 = new Subtask("New sub1",
                "Make sub 1",
                Duration.ofMinutes(20L),
                LocalDateTime.of(2023, 3, 1, 0, 0),
                epic1
        );

        manager.addTaskEpic(epic1);
        manager.addTaskSub(subtask3);

        manager.doneSub(5);

        System.out.println(manager.getTaskById(4)); //Получение любого таска по айди*/


        var newManager = new HttpTaskManager();

        newManager.load();

        System.out.println(newManager.getTasksTree());
        System.out.println(newManager.printHistory());


    }
}

