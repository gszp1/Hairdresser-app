package messages.requests;

import messages.Message;
import messages.MessageType;
import messages.responses.VisitsListResponse;
import model.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VisitsListRequest extends Message implements Serializable {

    public VisitsListRequest(String clientId) {
        super(MessageType.VISITS_LIST_REQUEST, clientId, "");
    }

    @Override
    public boolean handleMessage(UserList userList, VisitsMap visitsMap) throws IOException {
        System.out.println("Received request for visits list.");
        Optional<User> user = userList.getUser(this.clientId);
        if (user.isPresent()) {
            user.get().getOutputStream().writeObject(
                    getVisitsListResponse(this.clientId, visitsMap)
            );
            return true;
        }
        return false;
    }

    public static VisitsListResponse getVisitsListResponse(String clientId, VisitsMap visitsMap) {
        return new VisitsListResponse(clientId, getVisitsDtos(visitsMap, clientId));
    }

    public static List<VisitDto> getVisitsDtos(VisitsMap visitsMap, String clientId) {
        return visitsMap.getVisits().stream()
                .map(visit ->
                        new VisitDto(
                                visit.getDay(),
                                visit.getHour(),
                                visit.getPhoneNumber(),
                                visit.getId(),
                                visit.getUser().getClientId(),
                                visit.getUser().getClientId().equals(clientId)
                        )
                ).collect(Collectors.toList());
    }

    public static void broadcastVisitList(UserList userList, VisitsMap visitsMap) {
        userList.getUsers().forEach(broadcastUser -> {
            try {
                broadcastUser.getOutputStream().writeObject(getVisitsListResponse(broadcastUser.getClientId(), visitsMap));
            } catch (IOException e) {
                System.out.printf("User %s does not exist\n", broadcastUser.getClientId());
            }
        });
    }
}
