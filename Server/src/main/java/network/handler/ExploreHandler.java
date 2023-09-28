package network.handler;

import config.Alerts;
import config.Config;
import database.DBSet.Context;
import database.Users;
import enums.AlertType;
import model.DB.DBPost;
import model.Post;
import model.User;
import responses.ExplorePageResponse;
import responses.Response;
import responses.ShowAlert;
import responses.ShowProfileResponse;

import java.util.ArrayList;
import java.util.Comparator;

public class ExploreHandler {
    private static final Config POST_CONFIG = Config.getConfig("posts");

    public Response explorePage(User user) {
        int REPORT_LIMIT = POST_CONFIG.getProperty(Integer.class, "Max_Report");
        ArrayList<Post> selectedPosts = new ArrayList<>();
        for (User postingUser :
                Context.get().Users.getAll()) {
            if (postingUser.isPublic() && !postingUser.equals(user) && postingUser.isActive()) {
                if (!Users.ifMuted(user, postingUser.getUsername())) {
                    for (Post post :
                            postingUser.getPosts()) {
                        if (post.getReports().size() < REPORT_LIMIT) {
                            selectedPosts.add(post);
                        }
                    }
                }
            }
        }
        selectedPosts.sort(Comparator.comparing(Post::getScore));
        ArrayList<DBPost> selectedDBPosts = new ArrayList<>();
        selectedPosts.forEach(post -> selectedDBPosts.add(new DBPost(post)));
        return new ExplorePageResponse(user, selectedDBPosts);
    }

    public Response search(String name, User user) {
        if (Users.userExists(name)) {
            return new ShowProfileResponse(Context.get().Users.get(name), user);
        }
        return new ShowAlert(Alerts.userNotExist(), AlertType.Error);
    }
}
