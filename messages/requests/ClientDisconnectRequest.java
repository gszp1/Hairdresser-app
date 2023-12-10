package messages.requests;

import messages.Message;
import messages.MessageType;
import messages.responses.ClientDisconnectResponse;
import model.User;
import model.UserList;
import model.VisitsMap;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

public class ClientDisconnectRequest extends Message implements Serializable {

    public ClientDisconnectRequest(String clientId) {
        super(MessageType.CLIENT_DISCONNECT_REQUEST, clientId, "");
    }

    @Override
    public boolean handleMessage(UserList userList, VisitsMap visitsMap) throws IOException {
        System.out.println("Received client disconnect request.");
        Optional<User> user = userList.getUser(this.clientId);
        if (user.isPresent()) {
            user.get().getOutputStream().writeObject(
                new ClientDisconnectResponse(user.get().getClientId(), true)
            );
            user.get().getSocket().close();
            userList.removeUser(user.get());
            return true;
        }
        return false;
    }
}
