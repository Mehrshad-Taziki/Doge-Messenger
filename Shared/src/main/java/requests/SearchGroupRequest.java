package requests;

import responses.Response;

public class SearchGroupRequest extends Request {
    private final String groupName;

    public SearchGroupRequest(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.searchGroup(groupName);
    }
}
