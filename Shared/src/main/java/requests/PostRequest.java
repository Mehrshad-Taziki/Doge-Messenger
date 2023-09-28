package requests;

import responses.Response;

import java.util.Base64;

public class PostRequest extends Request {
    private final String text;
    private final String imageInString;


    public PostRequest(String text, byte[] imageInByte) {
        this.text = text;
        if (imageInByte == null) {
            imageInString = "";
        } else {
            imageInString = Base64.getEncoder().encodeToString(imageInByte);
        }
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.post(text, imageInString);
    }
}
