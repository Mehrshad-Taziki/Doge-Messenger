package responses;

import enums.ChatContainerType;
import model.DB.DBMessage;

import java.util.ArrayList;

public class UpdateChatResponse extends Response {
    private final ChatContainerType type;
    private final ArrayList<DBMessage> messages;

    public UpdateChatResponse(ChatContainerType type, ArrayList<DBMessage> messages) {
        this.type = type;
        this.messages = messages;
    }


    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.updateChat(type, messages);
    }
}
