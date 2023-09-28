package responses;

import model.DB.DBUser;
import model.User;

import java.util.ArrayList;

public class PersonalPageResponse extends Response {
    private final DBUser user;
    private ArrayList<DBUser> followers;
    private ArrayList<DBUser> followings;
    private ArrayList<DBUser> blacklist;
    private ArrayList<DBUser> requests;

    public PersonalPageResponse(User user, ArrayList<DBUser> requests) {
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
        responseHandler.personalPage(user, followers, followings, blacklist, requests);
    }


}
