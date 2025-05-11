import com.sun.net.httpserver.HttpServer;
import httphandlers.*;
import managing.Managers;
import managing.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class HttpTaskServer {
    private static HttpServer server;
    private static TaskManager manager;

    public HttpTaskServer(TaskManager manager) throws IOException {
    this.manager = manager;
    ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
    server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new BaseHttpHandler());
        server.createContext("/task", new TaskHttpHandler(manager));
        server.createContext("/epic", new EpicHttpHandler(manager));
        server.createContext("/subtask", new SubtaskHttpHandler(manager));
        server.createContext("/history", new HistoryHttpHandler(manager));
        server.createContext("/priority", new PriorityHttpHandler(manager));
    }

    /*
        public static void main(String[] args) throws Exception {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
            manager = Managers.createTaskManager();
            start();
            stop();
        }
    */
    public static void start() {
        server.start();
    }

    public static void stop() {
        server.stop(60);
    }
}