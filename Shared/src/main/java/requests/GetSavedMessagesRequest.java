package requests;

import responses.Response;

public class GetSavedMessagesRequest extends Request {
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.getSavedMessage();
    }
}
