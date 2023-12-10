package client;

import messages.responses.AddVisitResponse;
import messages.responses.ClientDisconnectResponse;
import messages.responses.RemoveVisitResponse;
import messages.responses.VisitsListResponse;

import java.util.function.Consumer;

public class MessageListener {

    private Consumer<AddVisitResponse> addVisitsHandler;

    private Consumer<ClientDisconnectResponse> clientDisconnectHandler;

    private Consumer<RemoveVisitResponse> removeVisitsHandler;

    private Consumer<VisitsListResponse> visitsListHandler;

    public void onVisitAdded(AddVisitResponse addVisitResponse) {
        addVisitsHandler.accept(addVisitResponse);
    }

    public void onClientDisconnected(ClientDisconnectResponse clientDisconnectResponse) {
        clientDisconnectHandler.accept(clientDisconnectResponse);
    }

    public void onVisitRemoved(RemoveVisitResponse removeVisitResponse) {
        removeVisitsHandler.accept(removeVisitResponse);
    }

    public void onVisitsList(VisitsListResponse visitsListResponse) {
        visitsListHandler.accept(visitsListResponse);
    }

    public void addAddVisitHandler(Consumer<AddVisitResponse> consumer) {
        addVisitsHandler = consumer;
    }

    public void addClientDisconnectedHandler(Consumer<ClientDisconnectResponse> consumer) {
        clientDisconnectHandler = consumer;
    }

    public void addRemoveVisitHandler(Consumer<RemoveVisitResponse> consumer) {
        removeVisitsHandler = consumer;
    }

    public void addVisitsListHandler(Consumer<VisitsListResponse> consumer) {
        visitsListHandler = consumer;
    }
}
