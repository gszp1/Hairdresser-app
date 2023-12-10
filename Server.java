import messages.Message;
import model.User;
import model.UserList;
import processor.MessageProcessor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {

    public static final int PORT = 5000;

    private final ServerSocket serverSocket;

    private final UserList userList = new UserList();

    private final ConcurrentLinkedQueue<Message> inputQueue = new ConcurrentLinkedQueue<>();

    private final Map<String, InputHandler> inputHandlerList = new HashMap<String, InputHandler>();

    private boolean serverStatus;

    public static final boolean SERVER_RUNNING = true;

    private final MessageProcessor messageProcessor;

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        serverStatus = SERVER_RUNNING;
        this.messageProcessor = new MessageProcessor(inputQueue, userList);
        this.messageProcessor.start();
    }

    public void start() {
        while (serverStatus == SERVER_RUNNING) {
            try {
                Socket socket = serverSocket.accept();
                User user = new User(new ObjectOutputStream(socket.getOutputStream()), socket, User.toClientId(socket));
                userList.addUser(user);
                InputHandler inputHandler = new InputHandler(
                    new ObjectInputStream(socket.getInputStream()),
                    user.getClientId(),
                    inputQueue
                );
                inputHandlerList.put(user.getClientId(), inputHandler);
                inputHandler.start();
            } catch (IOException e) {
                System.out.println("IO error occurred during establishing connection with client.");
            }
        }
    }

}
