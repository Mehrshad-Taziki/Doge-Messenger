package requests;

import responses.Response;

public class TimeLineRequest extends Request{
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.timeLinePage();
    }
}
