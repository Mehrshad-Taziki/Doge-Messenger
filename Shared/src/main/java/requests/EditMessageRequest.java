package requests;

import responses.Response;

public class EditMessageRequest extends Request {
    private final int messageID;
    private final String text;

    public EditMessageRequest(int messageID, String text) {
        this.messageID = messageID;
        this.text = text;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.editMessage(messageID, text);
    }
}
