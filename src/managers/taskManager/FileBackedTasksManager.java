package managers.taskManager;
import enums.Status;
import enums.Type;
import managers.exception.ManagerSaveException;
import tasks.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static tasks.SingleTask.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    public static final Path filePath = Path.of("resources/TaskHistory.csv");

    @Override
    public void addTask(SingleTask singleTaskToCreate) {
        super.addTask(singleTaskToCreate);
        save();
    }

    @Override
    public void addTaskEpic(Epic newEpic) {
        super.addTaskEpic(newEpic);
        save();
    }

    @Override
    public void addTaskSub(Subtask subtask) {
        super.addTaskSub(subtask);
        save();
    }

    @Override
    public Map<Integer, SingleTask> getSingleTasks() {
        Map<Integer, SingleTask> singleTasks = super.getSingleTasks();
        save();

        return singleHash;
    }

    @Override
    public Map<Integer, Epic> getEpicTasks() {
        Map<Integer, Epic> epicTasks = super.getEpicTasks();
        save();

        return epicHash;
    }

    @Override
    public Map<Integer, Subtask> getSubsTasks() {
        Map<Integer, Subtask> subTasks = super.getSubsTasks();
        save();

        return subsHash;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> task = super.getAllTasks();
        save();

        return task;
    }

    @Override
    public Set<Task> getTasksTree() {
        Set<Task> task = super.getTasksTree();
        save();

        return task;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();

        return task;
    }

    @Override
    public List<Subtask> getEpicsSubs(int id) {
        List<Subtask> subs = super.getEpicsSubs(id);
        save();

        return subs;
    }

    @Override
    public void removeById(int removeId) {
        super.removeById(removeId);
        save();
    }

    @Override
    public void removeAll() {
        super.removeAll();
        save();
    }

    protected void save() {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile(), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(new FileWriter(filePath.toFile(), StandardCharsets.UTF_8))) {
            List<Task> listTasks = new ArrayList<>(getTasksTree());
            List<Task> listHistory = inMemoryHistoryManager.getHistory();

            if (br.readLine() == null) {
                String header = "id,type,name,status,description,duration,startTime,finishTime,epic" + "\n";
                bw.write(header);
            }

            for (Task task : listTasks) {

                bw.write(task + "\n");
            }

            String values = historyIdsToString(listHistory);

            bw.write('\n' + values);

        } catch (IOException e) {

            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    public static FileBackedTasksManager loadFromFile(Path file) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

        try {
            String fileName = Files.readString(file);
            String[] lines = fileName.split("\n");

            for (int i = 1; i < lines.length - 2; i++) {
                Task task = fromString(lines[i]);
                fileBackedTasksManager.taskById.put(task.getId(), task);
            }

        } catch (IOException e) {

            throw new ManagerSaveException("Ошибка загрузки из файла");
        }
        return fileBackedTasksManager;
    }

    public static String historyIdsToString(List<Task> manager) {
        StringBuilder sb = new StringBuilder();

        for (Task task : manager) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public static Task fromString(String value) {

        int epicID = 0;
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        Type type = Type.valueOf(values[1]);
        String name = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        Duration duration = Duration.ZERO;
        LocalDateTime startTime = null;
        if (LocalDateTime.parse(values[6]) != null) {
            String dur = values[5].substring(2, values[5].length() - 1);
            duration = Duration.ofMinutes(Long.parseLong(dur));
            startTime = LocalDateTime.parse(values[6]);
        }

        if (Type.valueOf(String.valueOf(type)).equals(Type.SUBTASK))
            epicID = Integer.parseInt(values[8]);

        if (Type.valueOf(String.valueOf(type)).equals(Type.SINGLE))
            return new SingleTask(id, name, description, status, duration, startTime);

        if (Type.valueOf(String.valueOf(type)).equals(Type.EPIC))
            return new Epic(id, name, description, status);

        if (Type.valueOf(String.valueOf(type)).equals(Type.SUBTASK))
            return new Subtask(id, name, description, status, duration, startTime, epicID);

        else
            throw new IllegalArgumentException("Данный формат таска не поддерживается");

    }


}
