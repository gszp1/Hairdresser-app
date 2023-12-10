package messages;

import model.UserList;
import model.VisitsMap;

import java.io.IOException;
import java.io.Serializable;

public abstract class Message implements Serializable {

    private MessageType messageType;

    protected String clientId;

    private String data;

    public Message(MessageType messageType, String clientId, String data) {
        this.messageType = messageType;
        this.clientId = clientId;
        this.data = data;
    }

    public abstract boolean handleMessage(UserList userList, VisitsMap visitsMap) throws IOException;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
