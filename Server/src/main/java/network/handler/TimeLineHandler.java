package network.handler;

import config.Config;
import database.Users;
import model.DB.DBPost;
import model.Post;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.Response;
import responses.TimeLinePageResponse;

import java.util.ArrayList;
import java.util.Comparator;

public class TimeLineHandler {
    private static final Logger logger = LogManager.getLogger(TimeLineHandler.class);
    private static final Config POST_CONFIG = Config.getConfig("posts");

    public Response timeLinePage(User user) {
        int REPORT_LIMIT = POST_CONFIG.getProperty(Integer.class, "Max_Report");
        ArrayList<Post> selectedPosts = new ArrayList<>();
        for (User postingUser :
                user.getFollowings()) {
            if (!Users.ifMuted(user, postingUser.getUsername())) {
                for (Post post :
                        postingUser.getPosts()) {
                    if (post.getReports().size() < REPORT_LIMIT) {
                        selectedPosts.add(post);
                    }
                }
            }
            for (Post post :
                    postingUser.getLiked()) {
                if (!Users.ifMuted(user, post.getWriter().getUsername())) {
                    if (!post.isComment() && post.getReports().size() < REPORT_LIMIT)
                        selectedPosts.add(post);
                }
            }
        }
        selectedPosts.sort(Comparator.comparing(Post::getScore));
        ArrayList<DBPost> selectedDBPosts = new ArrayList<>();
        selectedPosts.forEach(post -> selectedDBPosts.add(new DBPost(post)));
        logger.info(user.getUsername() + "/" + user.getId() + " Selected Posts Loaded");
        return new TimeLinePageResponse(user, selectedDBPosts);
    }

}
