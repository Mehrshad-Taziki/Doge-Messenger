package requests;

import responses.Response;

public class SearchProfileRequest extends Request {
    private final String username;

    public SearchProfileRequest(String username) {
        this.username = username;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.search(username);
    }
}
