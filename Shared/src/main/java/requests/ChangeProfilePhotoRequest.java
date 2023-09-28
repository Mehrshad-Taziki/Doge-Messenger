package requests;

import responses.Response;

import java.util.Base64;

public class ChangeProfilePhotoRequest extends Request {
    private final String imageInString;

    public ChangeProfilePhotoRequest(byte[] imageInByte) {
        if (imageInByte == null) {
            imageInString = "";
        } else {
            imageInString = Base64.getEncoder().encodeToString(imageInByte);
        }
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.editProfilePhoto(imageInString);
    }
}
