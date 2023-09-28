package requests;

import responses.Response;

public class ChatBoxRequest extends Request{
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.chatBox();
    }
}
