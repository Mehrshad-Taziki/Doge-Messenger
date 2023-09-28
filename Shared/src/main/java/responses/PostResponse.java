package responses;

import model.DB.DBPost;
import model.Post;

public class PostResponse extends Response {
    DBPost post;

    public PostResponse(Post post) {
        this.post = new DBPost(post);
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.showPost(post);
    }
}
