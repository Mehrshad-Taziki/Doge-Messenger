package requests;

import responses.Response;

public class CommentRequest extends Request {
    private final int motherPostID;
    private final String text;

    public CommentRequest(int motherPostID, String text) {
        this.motherPostID = motherPostID;
        this.text = text;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.commentOn(text, motherPostID);
    }
}
