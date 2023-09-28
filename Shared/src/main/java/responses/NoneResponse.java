package responses;

public class NoneResponse extends Response{
    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.none();
    }
}
