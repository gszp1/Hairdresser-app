package client;

import messages.requests.*;
import messages.responses.AddVisitResponse;
import messages.responses.ClientDisconnectResponse;
import messages.responses.RemoveVisitResponse;
import messages.responses.VisitsListResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class ServerConnector extends Thread {

    public static final String SERVER_ADDRESS = "localhost";

    public static final int SERVER_PORT = 5000;

    private static ObjectOutputStream outputStream;

    private static ObjectInputStream inputStream;

    private final MessageListener messageListener;

    private final Socket socket;

    public ServerConnector(MessageListener messageListener) throws IOException {
        this.messageListener = messageListener;
        this.socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        listenOnSocket();
    }

    public void listenOnSocket() {
        try {
            while (!Thread.interrupted()) {
                Object object = inputStream.readObject();
                if (object instanceof ClientDisconnectResponse) {
                    messageListener.onClientDisconnected((ClientDisconnectResponse) object);
                } else if (object instanceof AddVisitResponse) {
                    messageListener.onVisitAdded((AddVisitResponse) object);
                } else if (object instanceof VisitsListResponse) {
                    messageListener.onVisitsList((VisitsListResponse) object);
                } else if (object instanceof RemoveVisitResponse) {
                    messageListener.onVisitRemoved((RemoveVisitResponse) object);
                }
            }
        } catch (SocketException e) {
            System.out.println("Client got disconnected");
            System.exit(0);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDisconnectMessage() throws IOException {
        outputStream.writeObject(new ClientDisconnectRequest(""));
    }

    public void sendAddVisit(Date date, int hour, String phoneNumber) throws IOException {
        outputStream.writeObject(new AddVisitRequest("", date, hour, phoneNumber));
    }

    public void sendListVisitsRequest() throws IOException {
        outputStream.writeObject(new VisitsListRequest(""));
    }

    public void sendRemoveVisit(String visitId) throws IOException {
        outputStream.writeObject(new RemoveVisitRequest("", visitId));
    }

    public Socket getSocket() {
        return socket;
    }
}
