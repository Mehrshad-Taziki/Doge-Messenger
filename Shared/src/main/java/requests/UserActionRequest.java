package requests;

import enums.UserAction;
import responses.Response;

public class UserActionRequest extends Request {
    private final String username;
    private final UserAction action;

    public UserActionRequest(String username, UserAction action) {
        this.username = username;
        this.action = action;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.takeUserAction(this);
    }

    public String getUsername() {
        return username;
    }

    public UserAction getAction() {
        return action;
    }
}
