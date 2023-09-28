package requests;

import responses.Response;

public class AddUserToContactRequest extends Request {
    private final String username;

    public AddUserToContactRequest(String username) {
        this.username = username;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.addToContact(username);
    }
}
