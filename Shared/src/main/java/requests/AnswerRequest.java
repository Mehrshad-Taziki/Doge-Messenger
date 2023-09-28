package requests;

import enums.RequestAnswer;
import responses.Response;

public class AnswerRequest extends Request {
    private final String username;
    private final RequestAnswer answer;

    public AnswerRequest(String username, RequestAnswer answer) {
        this.username = username;
        this.answer = answer;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.answer(username, answer);
    }
}
