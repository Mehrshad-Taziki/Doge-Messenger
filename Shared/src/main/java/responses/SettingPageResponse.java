package responses;

import model.DB.DBUser;
import model.User;

public class SettingPageResponse extends Response {
    DBUser user;

    public SettingPageResponse(User user) {
        this.user = new DBUser(user);
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.settingPage(user);
    }
}
