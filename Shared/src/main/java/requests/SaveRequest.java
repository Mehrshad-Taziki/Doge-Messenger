package requests;

import responses.Response;

import java.util.Base64;

public class SaveRequest extends Request {
    private final String text;
    private final String imageInString;


    public SaveRequest(String text, byte[] imageInByte) {
        this.text = text;
        if (imageInByte == null) {
            imageInString = "";
        } else {
            imageInString = Base64.getEncoder().encodeToString(imageInByte);
        }    }

    public String getText() {
        return text;
    }

    public String getImageInString() {
        return imageInString;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.save(this);
    }
}
