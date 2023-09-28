package network.handler;

import config.Alerts;
import config.Config;
import database.DBSet.Context;
import database.ID;
import database.Images;
import database.saver.MainSaver;
import enums.AlertType;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.Post;
import model.User;
import responses.*;
import tools.ImageHandler;

import java.util.Base64;

public class PostHandler {
    private static final Config POST_CONFIG = Config.getConfig("posts");

    public Response like(Post post, User user) {
        user.getLiked().add(post);
        post.getLikes().add(user);
        MainSaver.get().User.save(user);
        MainSaver.get().Post.save(post);
        return new PostResponse(post);
    }

    public Response unLike(Post post, User user) {
        user.getLiked().remove(post);
        post.getLikes().remove(user);
        MainSaver.get().User.save(user);
        MainSaver.get().Post.save(post);
        return new PostResponse(post);
    }

    public Response rePost(Post post, User user) {
        String rePoster = user.getUsername();
        String provider = post.getProvider();
        User writer = post.getWriter();
        if (rePoster.equals(writer.getUsername())) {
            return new ShowAlert(Alerts.repostOwnPost(), AlertType.Error);
        } else if (!writer.getUsername().equals(provider)) {
            return new ShowAlert(Alerts.repostReposted(), AlertType.Error);
        } else {
            user.getPosts().add(post.getReposted(user.getUsername()));
            MainSaver.get().Post.save(post.getReposted(user.getUsername()));
            MainSaver.get().User.save(user);
            return new ShowAlert(Alerts.reposted(), AlertType.Info);
        }
    }

    public Response report(Post post, User user) {
        if (!post.getReports().contains(user)) {
            post.getReports().add(user);
            if (post.getReports().size() >= POST_CONFIG.getProperty(Integer.class, "Max_Report")) {
                User writer = post.getWriter();
                writer.getPosts().remove(post);
            }
            MainSaver.get().Post.save(post);
            MainSaver.get().User.save(post.getWriter());
            return new ShowAlert(Alerts.reported(), AlertType.Info);
        } else {
            return new ShowAlert(Alerts.alreadyReported(), AlertType.Error);
        }
    }


    public Response seeComments(Post post, User user) {
        if (post.getComments().isEmpty()) {
            return new ShowAlert(Alerts.noComment(), AlertType.Info);
        }
        return new CommentsResponse(post.getComments(), post, user);
    }

    public Response commentOn(String text, int motherPostID, User user) {
        Post motherPost = Context.get().Posts.get(String.valueOf(motherPostID));
        Post comment = new Post(user, text, true, 0);
        motherPost.addComment(comment);
        MainSaver.get().Post.save(comment);
        MainSaver.get().Post.save(motherPost);
        return new ShowAlert(Alerts.commented(), AlertType.Info);
    }

    public Response post(String text, String imageInString, User user) {
        Post post = new Post(user, text, false, 0);
        Context.get().Posts.getAll().add(post);
        if (!imageInString.equals("")) {
            byte[] imageInBytes = Base64.getDecoder().decode(imageInString);
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInBytes), null);
            Images.save(image);
            post.setImageID(ID.getLastGeneratedID());
            post.setImageInString(imageInString);
        }
        user.addPost(post);
        MainSaver.get().Post.save(post);
        MainSaver.get().User.save(user);
        return new UpdatePostListResponse(user.getPosts());
    }
}
