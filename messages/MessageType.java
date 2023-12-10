package messages;

public enum MessageType {
    CLIENT_DISCONNECT_REQUEST("CLIENT_DISCONNECT_REQUEST"),
    CLIENT_DISCONNECT_RESPONSE("CLIENT_DISCONNECT_RESPONSE"),
    ADD_VISIT_REQUEST("ADD_VISIT_REQUEST"),
    ADD_VISIT_RESPONSE("ADD_VISIT_RESPONSE"),
    REMOVE_VISIT_REQUEST("REMOVE_VISIT_REQUEST"),
    REMOVE_VISIT_RESPONSE("REMOVE_VISIT_RESPONSE"),
    VISITS_LIST_REQUEST("CLIENT_LIST_REQUEST"),
    VISITS_LIST_RESPONSE("CLIENT_LIST_RESPONSE");


    private final String header;

    MessageType(String header){
        this.header = header;
    }

    public String getHeader() {
        return header;
    }


}
