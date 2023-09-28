package requests;

import responses.Response;

public class SearchContactRequest extends Request{
    private final String groupName;

    public SearchContactRequest(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.searchContact(groupName);
    }
}
