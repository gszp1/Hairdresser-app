package messages.responses;

import messages.Message;
import messages.MessageType;
import model.UserList;
import model.VisitsMap;

import java.io.Serializable;

public class RemoveVisitResponse extends Message implements Serializable {

    private final boolean success;

    public RemoveVisitResponse(String clientId, boolean success) {
        super(MessageType.REMOVE_VISIT_REQUEST, clientId, String.format("%s", success));
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
