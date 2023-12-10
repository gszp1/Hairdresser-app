package messages.responses;

import messages.Message;
import messages.MessageType;
import model.UserList;
import model.VisitsMap;

import java.io.Serializable;

public class ClientDisconnectResponse extends Message implements Serializable {

    private final boolean success;

    public ClientDisconnectResponse(String clientId, boolean success){
        super(MessageType.CLIENT_DISCONNECT_RESPONSE, clientId, Boolean.toString(success));
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }


    @Override
    public boolean handleMessage(UserList userList, VisitsMap visitsMap) {
        return false;
    }
}
