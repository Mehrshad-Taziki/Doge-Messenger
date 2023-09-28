package requests;

import responses.Response;

public class LogOutRequest extends Request{
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.logout();
    }
}
