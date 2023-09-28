package sender;

import requests.Request;
import responses.Response;
import tools.gson.Deserializer;
import tools.gson.Serializer;
import com.google.gson.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketRequestSender implements RequestSender {
    private final Socket socket;
    private final PrintStream printStream;
    private final Scanner scanner;
    private final Gson gson;

    public SocketRequestSender(Socket socket) throws IOException {
        this.socket = socket;
        this.printStream = new PrintStream(socket.getOutputStream(), true);
        this.scanner = new Scanner(socket.getInputStream());
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Response.class, new Deserializer<>())
                .registerTypeAdapter(Request.class, new Serializer<>())
                .create();
    }

    @Override
    public Response sendRequest(Request request) {
        System.out.println("sender1");
        String requestString = gson.toJson(request, Request.class);
        System.out.println("sender2");
        printStream.println(requestString);
        System.out.println("sender3");
        String responseString = scanner.nextLine();
        System.out.println("sender4");
        return gson.fromJson(responseString, Response.class);
    }

    public void close() {
        try {
            printStream.close();
            scanner.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
