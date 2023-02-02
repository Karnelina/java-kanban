import Tasks.*;
import managers.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        SingleTask.ToCreate singleTaskToCreate = new SingleTask.ToCreate("Pure safe task");
        manager.addTask(singleTaskToCreate);

        System.out.println(manager.getAllTasks());

        manager.doneSingleTask(0);

        Epic.ToCreate epic = new Epic.ToCreate("New Epic");
        Subtask.ToCreate subtask1 = new Subtask.ToCreate("New sub1", epic);
        Subtask.ToCreate subtask2 = new Subtask.ToCreate("New sub2", epic);
        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);
        manager.addTaskSub(subtask2);

        System.out.println(manager.getAllTasks());

        manager.doneSub(2); // Завершение саба
        manager.doneEpic(); // Проверка на прогресс эпика

        Epic.ToCreate epic1 = new Epic.ToCreate("New Epic1");
        Subtask.ToCreate subtask3 = new Subtask.ToCreate("New sub1", epic1);
        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask3);

        System.out.println(manager.getAllTasks());

        manager.doneSub(5);
        manager.doneEpic();

        System.out.println(manager.getAllTasks());

        System.out.println(manager.getEpicsSubs(1)); // Получение сабов у определнного эпика

        System.out.println(manager.getTaskById(1)); //Получение любого таска по айди
        System.out.println(manager.getTaskById(3));
        System.out.println(manager.getTaskById(0));
        System.out.println(manager.getTaskById(4));
        System.out.println(manager.getTaskById(5));
        System.out.println(manager.getTaskById(0));
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getTaskById(3));
        System.out.println(manager.getTaskById(4));
        System.out.println(manager.getTaskById(5));
        System.out.println(manager.getTaskById(0));

        manager.printHistory(); // проверка, чтобы не было повторов

        manager.removeById(1); //удаление любого таска, если эпик, то вместе с сабами.

        manager.printHistory(); // Проверка удаления истории

        System.out.println(manager.getAllTasks());

        manager.removeAll(); // Удаление всех тасков из мапы

        manager.printHistory();
    }
}
