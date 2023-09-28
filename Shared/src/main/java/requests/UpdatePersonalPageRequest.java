package requests;

import responses.Response;

public class UpdatePersonalPageRequest extends Request {
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.updatePersonalPage();
    }
}
