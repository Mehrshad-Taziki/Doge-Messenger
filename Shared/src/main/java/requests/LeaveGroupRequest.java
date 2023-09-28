package requests;

import responses.Response;

public class LeaveGroupRequest extends Request {


    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.leaveGroup();
    }
}
