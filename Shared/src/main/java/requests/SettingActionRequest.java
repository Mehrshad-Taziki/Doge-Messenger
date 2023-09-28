package requests;

import enums.SettingAction;
import responses.Response;

public class SettingActionRequest extends Request {
    private final SettingAction action;

    public SettingActionRequest(SettingAction action) {
        this.action = action;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.takeSettingAction(action);
    }
}
