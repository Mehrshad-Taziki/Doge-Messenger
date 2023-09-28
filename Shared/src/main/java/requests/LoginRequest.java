package requests;

import responses.Response;

public class LoginRequest extends Request{
    private final String userName, password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.login(this);
    }
}
