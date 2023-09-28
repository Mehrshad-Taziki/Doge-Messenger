package requests;

import responses.Response;

public class SettingPageRequest extends Request{
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.settingPage();
    }
}
