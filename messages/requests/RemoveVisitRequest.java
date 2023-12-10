package messages.requests;

import messages.Message;
import messages.MessageType;
import messages.responses.RemoveVisitResponse;
import model.User;
import model.UserList;
import model.Visit;
import model.VisitsMap;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

public class RemoveVisitRequest extends Message implements Serializable {

    private final String visitId;

    public RemoveVisitRequest(String clientId, String visitId) {
        super(MessageType.REMOVE_VISIT_REQUEST, clientId, String.format("%s", visitId));
        this.visitId = visitId;
    }

    public String getVisitId() {
        return visitId;
    }

    @Override
    public boolean handleMessage(UserList userList, VisitsMap visitsMap) throws IOException {
        System.out.println("Received visit removal request.");
        Optional<User> user = userList.getUser(this.clientId);
        if (!user.isPresent()) {
            return false;
        }
        Optional<String> maybeError = validate(visitsMap);
        if (maybeError.isPresent()) {
            user.get().getOutputStream().writeObject(
                    new RemoveVisitResponse(clientId, false)
            );
        }
        boolean wasVisitDeleted = visitsMap.removeVisit(visitId);
        user.get().getOutputStream().writeObject(
                new RemoveVisitResponse(clientId, wasVisitDeleted)
        );
        if (wasVisitDeleted) {
            VisitsListRequest.broadcastVisitList(userList, visitsMap);
        }
        return true;
    }

    private Optional<String> validate(VisitsMap visitsMap) {
        Optional<Visit> visit = visitsMap.getVisitById(visitId);
        if (!visit.isPresent()) {
            return Optional.of("Visit does not exist");
        }
        if (!visit.get().getUser().getClientId().equals(clientId)) {
            return Optional.of("Requester is not a creator of visit");
        }
        return Optional.empty();
    }
}
