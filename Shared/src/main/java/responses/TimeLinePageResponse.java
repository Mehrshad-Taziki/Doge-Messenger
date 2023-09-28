package responses;

import model.DB.DBPost;
import model.DB.DBUser;
import model.User;

import java.util.ArrayList;

public class TimeLinePageResponse extends Response{
    private final DBUser user;
    private final ArrayList<DBPost> selectedPost;

    public TimeLinePageResponse(User user, ArrayList<DBPost> selectedPost) {
        this.user = new DBUser(user);
        this.selectedPost = selectedPost;
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.timeLinePage(user, selectedPost);
    }
}
