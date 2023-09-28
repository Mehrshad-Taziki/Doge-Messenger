package requests;

import responses.Response;

public class UpdateChatListRequest extends Request{
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.updateChatList();
    }
}
