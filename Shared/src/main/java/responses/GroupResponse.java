package responses;

import model.DB.DBGroup;
import model.Group;

public class GroupResponse extends Response {
    private final DBGroup group;

    public GroupResponse(Group group) {
        this.group = new DBGroup(group);
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.showGroup(group);
    }
}
