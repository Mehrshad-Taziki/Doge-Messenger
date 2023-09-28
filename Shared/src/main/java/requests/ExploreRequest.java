package requests;

import responses.Response;

public class ExploreRequest extends Request {
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.explorePage();
    }
}
