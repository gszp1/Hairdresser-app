package messages.responses;

import messages.Message;
import messages.MessageType;
import model.UserList;
import model.VisitDto;
import model.VisitsMap;

import java.io.Serializable;
import java.util.List;

public class VisitsListResponse extends Message implements Serializable {

    private final List<VisitDto> visits;

    public VisitsListResponse(String clientId, List<VisitDto> visits) {
        super(MessageType.VISITS_LIST_RESPONSE, clientId, String.format("%s", visits));
        this.visits = visits;
    }

    public List<VisitDto> getVisits() {
        return visits;
    }

    @Override
    public boolean handleMessage(UserList userList, VisitsMap visitsMap) {
        return false;
    }
}
