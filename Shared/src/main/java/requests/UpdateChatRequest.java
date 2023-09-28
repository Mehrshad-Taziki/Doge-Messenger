package requests;

import responses.Response;

public class UpdateChatRequest extends Request{

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.updateChat();
    }
}
