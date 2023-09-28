package requests;

import responses.Response;

public class UserEditRequest extends Request {
    private final boolean publicNumber;
    private final boolean publicEmail;
    private final boolean publicBirthday;
    private final boolean isPublic;
    private final int lastSeenType;
    private final String password;

    public UserEditRequest(boolean publicNumber, boolean publicEmail, boolean publicBirthday, boolean isPublic, int lastSeenType, String password) {
        this.publicNumber = publicNumber;
        this.publicEmail = publicEmail;
        this.publicBirthday = publicBirthday;
        this.isPublic = isPublic;
        this.lastSeenType = lastSeenType;
        this.password = password;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.editUserSetting(this);
    }

    public boolean isPublicNumber() {
        return publicNumber;
    }

    public boolean isPublicEmail() {
        return publicEmail;
    }

    public boolean isPublicBirthday() {
        return publicBirthday;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public int getLastSeenType() {
        return lastSeenType;
    }

    public String getPassword() {
        return password;
    }
}
