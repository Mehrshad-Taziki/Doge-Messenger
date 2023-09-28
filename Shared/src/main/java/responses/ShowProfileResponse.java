package responses;

import model.DB.DBUser;
import model.User;

public class ShowProfileResponse extends Response {
    private final DBUser user;
    private final boolean ifFollowed;
    private final boolean ifBlocked1;
    private final boolean ifBlocked2;
    private final boolean ifRequested;
    private final boolean ifMuted;
    private final boolean hasAccess;

    public ShowProfileResponse(User user, User viewer) {
        this.user = new DBUser(user);
        ifFollowed = viewer.getFollowings().contains(user);
        ifBlocked1 = user.getBlocked().contains(viewer);
        ifBlocked2 = viewer.getBlocked().contains(user);
        ifRequested = user.getRequests().contains(viewer.getUsername());
        ifMuted = viewer.getMuted().contains(user);
        hasAccess = hasAccess();
    }

    public boolean hasAccess() {
        if (ifBlocked1) {
            return false;
        } else if (ifBlocked2) {
            return false;
        } else if (user.isPublic()) {
            return true;
        } else return ifFollowed;
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.showProfile(this);
    }

    public DBUser getUser() {
        return user;
    }

    public boolean isIfFollowed() {
        return ifFollowed;
    }

    public boolean isIfBlocked1() {
        return ifBlocked1;
    }

    public boolean isIfBlocked2() {
        return ifBlocked2;
    }

    public boolean isIfRequested() {
        return ifRequested;
    }

    public boolean isIfMuted() {
        return ifMuted;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }
}
