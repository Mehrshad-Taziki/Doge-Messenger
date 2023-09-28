package requests;

import responses.Response;

public class PersonalPageRequest extends Request{
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.personalPage();
    }
}
