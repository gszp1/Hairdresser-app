package messages.responses;

import messages.Message;
import messages.MessageType;
import model.UserList;
import model.VisitsMap;

import java.io.Serializable;

public class AddVisitResponse  extends Message implements Serializable {

    private String visitId;

    private boolean success;

    public AddVisitResponse(String clientId, String visitId, boolean success) {
        super(MessageType.ADD_VISIT_RESPONSE, clientId, String.format("%s %s", visitId, success));
        this.visitId = visitId;
        this.success = success;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean handleMessage(UserList userList, VisitsMap visitsMap) {
        return false;
    }
}
