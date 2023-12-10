import messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputHandler extends Thread{

    private final ObjectInputStream socketInput;

    private final String clientId;

    private final ConcurrentLinkedQueue<Message> inputQueue;

    public InputHandler(ObjectInputStream socketInput, String clientId, ConcurrentLinkedQueue<Message> inputQueue) {
        this.clientId = clientId;
        this.socketInput = socketInput;
        this.inputQueue = inputQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Message message = (Message) socketInput.readObject();
                message.setClientId(clientId);
                inputQueue.add(message);
            }
        } catch (SocketException ex) {
            System.out.printf("Client %s got disconnected\n", clientId);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
