package processor;

import messages.Message;
import model.UserList;
import model.VisitsMap;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageProcessor extends Thread{

    private final ConcurrentLinkedQueue<Message> inputQueue;

    private final UserList userList;

    private final VisitsMap visitsMap = new VisitsMap();

    public MessageProcessor(ConcurrentLinkedQueue<Message> inputQueue, UserList userList) {
        this.inputQueue = inputQueue;
        this.userList = userList;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                if (!inputQueue.isEmpty()) {
                    Message message = inputQueue.remove();
                    message.handleMessage(userList, visitsMap);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
