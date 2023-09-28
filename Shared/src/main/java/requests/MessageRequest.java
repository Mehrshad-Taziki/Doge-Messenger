package requests;
import responses.Response;

import java.util.Base64;

public class MessageRequest extends Request {
    private final String text;
    private final int time;
    private final  String imageInString;

    public MessageRequest(String text, String time, byte[] imageInByte) {
        this.text = text;
        this.time = Integer.parseInt(time);
        if (imageInByte == null) {
            imageInString = "";
        } else {
            imageInString = Base64.getEncoder().encodeToString(imageInByte);
        }
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.sendMessage(this);
    }

    public String getText() {
        return text;
    }

    public int getTime() {
        return time;
    }

    public String getImageInString() {
        return imageInString;
    }

}
