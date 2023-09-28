package responses;

import model.DB.DBPost;
import model.DB.DBUser;
import model.Post;

import java.util.ArrayList;

public class UpdatePostListResponse extends Response {
    private final ArrayList<DBPost> posts;

    public UpdatePostListResponse(ArrayList<Post> posts) {
        this.posts = new ArrayList<>();
        posts.forEach(post -> this.posts.add(new DBPost(post)));
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.updatePostList(posts);
    }
}
