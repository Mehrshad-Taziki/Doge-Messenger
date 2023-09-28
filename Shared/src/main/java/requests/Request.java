package requests;

import responses.Response;

abstract public class Request {
    private int token;

    public abstract Response takeAct(RequestHandler requestHandler);

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
