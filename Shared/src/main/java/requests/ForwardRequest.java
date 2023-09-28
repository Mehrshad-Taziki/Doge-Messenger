package requests;

import enums.ChatContainerType;
import responses.Response;

public class ForwardRequest extends Request {
    private final String id;
    private final ChatContainerType type;
    private final int postID;

    public ForwardRequest(String id, ChatContainerType type, int postID) {
        this.id = id;
        this.type = type;
        this.postID = postID;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.forward(id, type, postID);
    }
}
