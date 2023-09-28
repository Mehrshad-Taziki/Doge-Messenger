package requests;

import enums.PostActions;
import responses.Response;

public class PostActionRequest extends Request {
    private final int id;
    private final PostActions action;

    public PostActionRequest(int id, PostActions action) {
        this.id = id;
        this.action = action;
    }

    public int getID() {
        return id;
    }

    public PostActions getAction() {
        return action;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.takePostAction(this);
    }
}
