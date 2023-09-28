package responses;

import model.DB.DBUser;
import model.User;

public class RebuildEditResponse extends Response {
    private final DBUser user;

    public RebuildEditResponse(User user) {
        this.user = new DBUser(user);
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.rebuildEdit(user);
    }
}
