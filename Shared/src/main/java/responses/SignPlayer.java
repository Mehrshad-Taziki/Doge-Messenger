package responses;

import model.DB.DBUser;

public class SignPlayer extends Response{
    private final int token;
    private final DBUser user;

    public SignPlayer(int token, DBUser user) {
        this.token = token;
        this.user = user;
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.signPlayer(token, user);
    }
}
