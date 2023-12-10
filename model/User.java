package model;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class User {

    private final transient ObjectOutputStream outputStream;

    private final transient Socket socket;

    private final String clientId;

    public User(ObjectOutputStream outputStream, Socket socket, String clientId) {
        this.outputStream = outputStream;
        this.socket = socket;
        this.clientId = clientId;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public String getClientId() {
        return clientId;
    }

    public static String toClientId(Socket socket) {
        return String.format("%s:%s", socket.getInetAddress().getHostName(), socket.getPort());
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public String toString() {
        return "model.User{" +
                "clientId='" + clientId + '\'' +
                '}';
    }
}
