package database.DBSet;

import database.loader.PostLoader;
import model.Post;

import java.util.LinkedList;

public class PostDBSet implements DBSet<Post> {
    private static final LinkedList<Post> loadedPosts = new LinkedList<>();
    private static final PostLoader postLoader = new PostLoader();

    @Override
    public Post get(String ID) {
        int id = Integer.parseInt(ID);
        for (Post post :
                Post.loadedPosts) {
            if (post.getId() == id) {
                return post;
            }
        }
        return postLoader.load(ID);
    }

    @Override
    public LinkedList<Post> getAll() {
        return loadedPosts;
    }
}
