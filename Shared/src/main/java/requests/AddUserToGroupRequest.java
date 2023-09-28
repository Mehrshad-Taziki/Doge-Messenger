package requests;

import responses.Response;

public class AddUserToGroupRequest extends Request {
    private final String username;

    public AddUserToGroupRequest(String username) {
        this.username = username;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.addToGroup(username);
    }
}
