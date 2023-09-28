package responses;

import model.DB.DBPrivateChat;
import model.PrivateChat;

public class PrivateChatResponse extends Response {
    private final DBPrivateChat privateChat;

    public PrivateChatResponse(PrivateChat privateChat) {
        this.privateChat = new DBPrivateChat(privateChat);
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.showPrivateChat(privateChat);
    }
}
