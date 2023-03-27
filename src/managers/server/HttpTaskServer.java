package managers.server;

import java.util.*;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import enums.Type;
import managers.taskManager.*;
import tasks.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.*;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson;
    private HttpExchange httpExchange;
    TaskManager manager = Managers.getDefault();
    private final HttpServer httpServer;
    private int responseCode = 404;
    private String responseString = "Использован не тот метод";

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);

        httpTaskServer.start();
    }
    public HttpTaskServer(TaskManager manager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        gson = Managers.getGson();
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен на " + PORT + " порту!");
    }

    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                ArrayList<String> regexRoots = new ArrayList<>();
                regexRoots.add("^/tasks$");
                regexRoots.add("^/tasks/task$");
                regexRoots.add("^/tasks/epic$");
                regexRoots.add("^/tasks/subtask$");
                regexRoots.add("^/tasks/task/\\d+$");
                regexRoots.add("^/tasks/history$");
                regexRoots.add("^/tasks/update/\\d+$");

                String path = exchange.getRequestURI().getPath();
                String requestMethod = exchange.getRequestMethod();
                httpExchange = exchange;

                switch (requestMethod) {
                    case "GET": {
                        if (Pattern.matches(regexRoots.get(0), path)) {
                            getPrioritizedTasks();
                            return;
                        }
                        if (Pattern.matches(regexRoots.get(1), path)) {
                            getTasks();
                            return;
                        }
                        if (Pattern.matches(regexRoots.get(2), path)) {
                            getEpics();
                            return;
                        }
                        if (Pattern.matches(regexRoots.get(3), path)) {
                            getSubtasks();
                            return;
                        }
                        if (Pattern.matches(regexRoots.get(4), path)) {
                            String pathId = path.replaceFirst("/tasks/task/", "");
                            int id = parsePathId(pathId);
                            getTasksByID(id);
                            return;
                        }
                        if (Pattern.matches(regexRoots.get(6), path)) {
                            String pathId = path.replaceFirst("/tasks/update/", "");
                            int id = parsePathId(pathId);
                            updateTask(id);
                            return;
                        }
                        if (Pattern.matches(regexRoots.get(5), path))
                            getHistory();
                        break;
                    }
                    case "POST": {
                        if (Pattern.matches(regexRoots.get(1), path)) {
                            addTask();
                            return;
                        }
                        if (Pattern.matches(regexRoots.get(2), path)) {
                            addEpicTask();
                            return;
                        }
                        if (Pattern.matches(regexRoots.get(3), path))
                            addSubTask();
                        break;
                    }
                    case "DELETE": {
                        if (Pattern.matches(regexRoots.get(4), path)) {
                            System.out.println("DELETE: началась обработка removeById");
                            String pathId = path.replaceFirst("/tasks/task/", "");
                            int id = parsePathId(pathId);
                            if (manager.getAllTasks().size() >= id) {
                                manager.removeById(id);
                                responseString = "Удалена таска id = " + id;
                                responseCode = 200;
                            } else {
                                responseString = "Получен некорректный id= " + pathId;
                                responseCode = 405;
                            }
                            return;
                        }
                        if (Pattern.matches(regexRoots.get(0), path)) {
                            System.out.println("DELETE: началась обработка removeAll");
                            manager.removeAll();
                            responseString = "Удалены все таски";
                            responseCode = 200;
                        }
                        break;
                    }
                    default: {
                        responseString = "Метод " + requestMethod + " не поддерживается.";
                        responseCode = 405;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                writeResponse(httpExchange, responseString, responseCode);
            }

        }
    }

        private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        private void addTask() throws IOException {
            System.out.println("POST: добавление SingleTask");
            InputStream is = httpExchange.getRequestBody();
            String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
            SingleTask single = gson.fromJson(body, SingleTask.class);

            manager.addTask(single);
            responseCode = 200;
            responseString = "Таска добавлена";

        }

        private void addEpicTask() throws IOException {
            System.out.println("POST: добавление Epic");
            InputStream is = httpExchange.getRequestBody();
            String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(body, Epic.class);

            manager.addTaskEpic(epic);
            responseCode = 200;
            responseString = "Эпик добавлен";
        }

        private void addSubTask() throws IOException {
            System.out.println("POST: добавление Subtask");
            InputStream is = httpExchange.getRequestBody();
            String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
            Subtask subtask = gson.fromJson(body, Subtask.class);

            manager.addTaskSub(subtask);
            responseCode = 200;
            responseString = "Сабтаск добавлен";
        }

        private void updateTask(int id) {
            System.out.println("GET: началась обработка updateTask");
            for (Task task : manager.getAllTasks()) {
                if (task.getId().equals(id)) {
                    if (task.getType().equals(Type.SINGLE)) {
                        manager.doneSingleTask(id);
                        responseCode = 200;
                        responseString = "update SingleTask " + id;
                    } else if (task.getType().equals(Type.SUBTASK)) {
                        manager.doneSub(id);
                        responseCode = 200;
                        responseString = "update SubTask " + id;
                    } else {
                        System.out.println("Не тот тип таски");
                    }
                } else {
                    responseCode = 404;
                    responseString = "Таски для update не найдены";
                }
            }
        }

        private void getPrioritizedTasks() {
            System.out.println("GET: началась обработка AllTasks");
            String allTasks = gson.toJson(manager.getTasksTree());

            if (!allTasks.isEmpty()) {
                responseCode = 200;
                responseString = allTasks;
            } else {
                responseCode = 404;
                responseString = "Таски не найдены";
            }
        }

        private void getTasksByID(int id) {
            System.out.println("GET: началась обработка AllTasks");
            String taskById = gson.toJson(manager.getTaskById(id));

            if (!taskById.isEmpty()) {
                responseCode = 200;
                responseString = taskById;
            } else {
                responseCode = 404;
                responseString = "Пост с идентификатором " + taskById + " не найден";
            }
        }

        private void getTasks() {
            System.out.println("GET: началась обработка getTasks");

            String allTasks = gson.toJson(manager.getSingleTasks());

            if (!allTasks.isEmpty()) {
                responseCode = 200;
                responseString = allTasks;
            } else {
                responseCode = 404;
                responseString = "Ошибка получения всех тасок";
            }
        }

        private void getEpics() {
            System.out.println("GET: началась обработка getEpic");

            String allEpics = gson.toJson(manager.getEpicTasks());

            if (!allEpics.isEmpty()) {
                responseCode = 200;
                responseString = allEpics;
            } else {
                responseCode = 404;
                responseString = "Ошибка получения всех эпиков";
            }
        }

        private void getSubtasks() {
            System.out.println("GET: началась обработка getSubtasks");

            String allSubTasks = gson.toJson(manager.getSubsTasks());

            if (!allSubTasks.isEmpty()) {
                responseCode = 200;
                responseString = allSubTasks;
            } else {
                responseCode = 404;
                responseString = "Ошибка получения всех сабтасок";
            }
        }

        private void getHistory() {
            System.out.println("GET: началась обработка getHistory");

            String history = gson.toJson(manager.printHistory());

            if (!history.isEmpty()) {
                responseCode = 200;
                responseString = history;
            } else {
                responseCode = 404;
                responseString = "Ошибка получения истории";
            }
        }

        private int parsePathId(String path) {
            try {
                return Integer.parseInt(path);
            } catch (NumberFormatException e) {
                return -1;
            }
        }

    }
