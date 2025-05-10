import com.sun.net.httpserver.HttpServer;
import httphandlers.*;
import managing.Managers;
import managing.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer server;
    private static TaskManager manager;

    public HttpTaskServer() throws IOException {
    //    manager = Managers.createTaskManager();
    //    server = HttpServer.create(new InetSocketAddress(8080), 0);
    }


    public static void main(String[] args) throws Exception {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        manager = Managers.createTaskManager();
        server.createContext("/", new BaseHttpHandler());
        server.createContext("/task", new TaskHttpHandler(manager));
        server.createContext("/epic", new EpicHttpHandler(manager));
        server.createContext("/subtask", new SubtaskHttpHandler(manager));
        server.createContext("/history", new HistoryHttpHandler(manager));
        server.createContext("/priority", new PriorityHttpHandler(manager));
        start();
    }

    public static void start() {
        server.start();
    }

    public static void stop() {
        server.stop(60);
    }
}