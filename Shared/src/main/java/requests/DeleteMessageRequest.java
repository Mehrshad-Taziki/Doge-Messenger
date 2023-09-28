package requests;

import responses.Response;

public class DeleteMessageRequest extends Request{
    private final int id;
    public DeleteMessageRequest(int id){
        this.id = id;
    }
    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.deleteMessage(id);
    }

}
