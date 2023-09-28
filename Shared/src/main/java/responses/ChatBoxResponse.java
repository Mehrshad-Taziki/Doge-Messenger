package responses;

import model.DB.DBUser;
import model.User;

public class ChatBoxResponse extends Response{
    private final DBUser viewer;

    public ChatBoxResponse(User viewer) {
        this.viewer = new DBUser(viewer);
    }


    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.chatBox(viewer);
    }
}
