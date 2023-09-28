package responses;

public class ResetResponse extends Response {
    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.reset();
    }
}
