package responses;

import model.DB.DBUser;
import model.User;

import java.util.ArrayList;

public class UpdatePersonalPageResponse extends Response {
    private final DBUser user;
    private final ArrayList<DBUser> followers;
    private final ArrayList<DBUser> followings;
    private final ArrayList<DBUser> blacklist;
    private final ArrayList<DBUser> requests;

    public UpdatePersonalPageResponse(User user, ArrayList<DBUser> requests) {
        this.user = new DBUser(user);
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.blacklist = new ArrayList<>();
        this.requests = requests;
        user.getFollowers().forEach(client -> this.followers.add(new DBUser(client)));
        user.getFollowings().forEach(client -> this.followings.add(new DBUser(client)));
        user.getBlocked().forEach(client -> this.blacklist.add(new DBUser(client)));
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.updatePersonalPage(user, followers, followings, blacklist, requests);
    }
}
