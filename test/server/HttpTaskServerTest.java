package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    private Gson gson = Managers.getGson();
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

        manager.addTask(singleTaskToCreate);
        manager.addTaskEpic(epic);
        manager.addTaskSub(subtask1);
        manager.addTaskSub(subtask2);


        manager.getTaskById(0);
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getTaskById(3);

        server.start();

    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    @Test
    void getTasksTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/task");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        var type = new TypeToken<ArrayList<SingleTask>>(){}.getType();
        List<SingleTask> tasks = gson.fromJson(response.body(), type);

        assertNotNull(tasks, "Таски не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество тасок");

    }

    @Test
    void getEpicsTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/epic");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        var type = new TypeToken<Map<Integer, Epic>>(){}.getType();
        Map<Integer, Epic> epics = gson.fromJson(response.body(), type);

        assertNotNull(epics);
        assertEquals(1, epics.size());

    }

    @Test
    void getSubtasksTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/subtask");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        var type = new TypeToken<Map<Integer, Subtask>>(){}.getType();
        Map<Integer, Subtask> subtasks = gson.fromJson(response.body(), type);

        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());

    }

    @Test
    void getTaskTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/task/0");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        var type = new TypeToken<Task>(){}.getType();
        Task task = gson.fromJson(response.body(), type);

        assertNotNull(task);
        assertEquals(singleTaskToCreate, task);

    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/history");

        var request = HttpRequest.newBuilder().
                uri(url).
                GET().
                build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        var type = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(response.body(), type);

        assertNotNull(tasks);
        assertEquals(4, tasks.size());

    }

    @Test
    void getPrioritizedTasksTest() throws IOException, InterruptedException {
        manager.addTask(singleSameSub1);

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        var type = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasks = gson.fromJson(response.body(), type);

        assertNotNull(tasks);
        assertEquals(4, tasks.size());

    }

    @Test
    void createTaskTest() throws IOException, InterruptedException {

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

    }

    @Test
    void createEpicTest() throws IOException, InterruptedException {

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

    }

    @Test
    void createSubtaskTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/subtask");

        var json = gson.toJson(subtask1);

        var body = HttpRequest.BodyPublishers.ofString(json);

        var request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

    @Test
    void deleteTaskTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/task/1");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

    @Test
    void deleteTasksTest() throws IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        var url = URI.create("http://localhost:8080/tasks/task");

        var request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        var type = new TypeToken<Map<Integer, Task>>(){}.getType();
        Map<Integer, Task> tasks = gson.fromJson(response.body(), type);

        assertNull(tasks);

    }

}
