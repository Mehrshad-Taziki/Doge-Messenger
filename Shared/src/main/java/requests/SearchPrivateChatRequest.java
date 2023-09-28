package requests;

import responses.Response;

public class SearchPrivateChatRequest extends Request {
    private final String username;

    public SearchPrivateChatRequest(String username) {
        this.username = username;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.searchPrivateChat(username);
    }
}
