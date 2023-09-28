package responses;

import model.DB.DBPost;
import model.DB.DBUser;
import model.Post;
import model.User;

import java.util.ArrayList;

public class CommentsResponse extends Response {
    private final ArrayList<DBPost> comments;
    private final DBPost motherPost;
    private final DBUser viewer;

    public CommentsResponse(ArrayList<Post> comments, Post motherPost, User viewer) {
        this.comments = new ArrayList<>();
        comments.forEach(post -> this.comments.add(new DBPost(post)));
        this.motherPost = new DBPost(motherPost);
        this.viewer = new DBUser(viewer);
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.showComments(comments, motherPost, viewer);
    }
}
