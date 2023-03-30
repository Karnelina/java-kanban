package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.exception.IntersectionException;
import managers.server.HttpTaskServer;
import managers.taskManager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private HttpTaskServer server;
    private final Gson gson = Managers.getGson();
    TaskManager manager;
    protected SingleTask singleTaskToCreate;
    protected Epic epic;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected SingleTask singleSameSub1;

    @BeforeEach
    void init() throws IOException {

        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);

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

        server.start();

    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    @Test
    void getTasksTest() throws IOException, InterruptedException {
        manager.addTask(singleTaskToCreate);

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/task");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        List<String> list = new ArrayList<>();
        list.add(gson.toJson(singleTaskToCreate));

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertNotNull(list);
        assertEquals(1, list.size());

    }

    @Test
    void getEpicsTest() throws IOException, InterruptedException {
        manager.addTaskEpic(epic);

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/epic");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        List<String> list = new ArrayList<>();
        list.add(gson.toJson(epic));

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertNotNull(list);
        assertEquals(1, list.size());

    }

    @Test
    void getSubtasksTest() throws IOException, InterruptedException {
        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);
        manager.addTaskSub(subtask2);

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/subtask");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<String> list = new ArrayList<>();
        list.add(gson.toJson(subtask1));
        list.add(gson.toJson(subtask2));

        assertEquals(200, response.statusCode());

        assertNotNull(list);
        assertEquals(2, list.size());

    }

    @Test
    void getTaskByIdTest() throws IOException, InterruptedException {
        manager.addTask(singleTaskToCreate);
        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);
        manager.addTaskSub(subtask2);

        Task taskEpic = manager.getTaskById(1);

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/task/0");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();


        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertEquals(4, manager.getAllTasks().size());
        assertEquals(taskEpic, manager.getTaskById(1));

    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        manager.addTask(singleTaskToCreate);
        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);
        manager.addTaskSub(subtask2);

        assertEquals(0, manager.printHistory().size());

        manager.getTaskById(0);
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getTaskById(3);

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/history");

        var request = HttpRequest.newBuilder().
                uri(url).
                GET().
                build();


        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(manager.printHistory());
        assertEquals(4, manager.printHistory().size());

    }

    @Test
    void getPrioritizedTasksTest() throws IOException, InterruptedException {
        manager.addTask(singleTaskToCreate);
        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);
        manager.addTaskSub(subtask2);
        manager.addTask(singleSameSub1);

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        final IntersectionException exception = assertThrows(
                IntersectionException.class,

                () -> manager.getTasksTree());

        assertEquals("Найдено пересечение между " + manager.getTaskById(4).getId() + " и " + manager.getTaskById(2).getId(),
                exception.getMessage());

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());


    }

    @Test
    void addSingleTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/task");

        var json = gson.toJson(singleTaskToCreate);

        var body = HttpRequest.BodyPublishers.ofString(json);

        var request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Таска добавлена", response.body());

    }

    @Test
    void addEpicAndSubTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/epic");

        var json = gson.toJson(epic);

        var body = HttpRequest.BodyPublishers.ofString(json);

        var request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("Эпик добавлен", response.body());

        var url1 = URI.create("http://localhost:8080/tasks/subtask");

        var json1 = gson.toJson(subtask1);
        var body1 = HttpRequest.BodyPublishers.ofString(json1);

        var request1 = HttpRequest.newBuilder()
                .uri(url1)
                .POST(body1)
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());

        assertEquals("Сабтаск добавлен", response1.body());

    }

    @Test
    void deleteTaskByIdTest() throws IOException, InterruptedException {
        manager.addTask(singleTaskToCreate);
        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);

        assertEquals(3, manager.getAllTasks().size());

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/task/0");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

    @Test
    void deleteAllTasksTest() throws IOException, InterruptedException {
        manager.addTask(singleTaskToCreate);
        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);

        assertEquals(3, manager.getAllTasks().size());

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

}
