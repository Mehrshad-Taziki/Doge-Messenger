package network.handler;

import database.DBSet.Context;
import database.ID;
import database.Images;
import database.saver.MainSaver;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.Notification;
import model.User;
import responses.NoneResponse;
import responses.Response;
import responses.ShowProfileResponse;
import tools.ImageHandler;

import java.util.Base64;

public class ProfileHandler {

    public void accept(String username, User user) {
        User answeredUser = Context.get().Users.get(username);
        user.getRequests().remove(username);
        if (answeredUser != null) {
            if (!user.getFollowers().contains(answeredUser))
                user.getFollowers().add(answeredUser);
            if (!answeredUser.getFollowings().contains(user))
                answeredUser.getFollowings().add(user);
            answeredUser.getNotification().add(Notification.type4(user.getUsername()));
            user.getNotification().add(Notification.type5(answeredUser.getUsername()));
            MainSaver.get().User.save(user);
            MainSaver.get().User.save(answeredUser);
        }
    }

    public void decline(int hidden, String username, User user) {
        User answeredUser = Context.get().Users.get(username);
        user.getRequests().remove(username);
        if (answeredUser != null) {
            if (hidden == 0) {
                answeredUser.getNotification().add(Notification.type1(user.getUsername()));
            }
            MainSaver.get().User.save(user);
            MainSaver.get().User.save(answeredUser);
        }
    }

    public Response follow(User actionedUser, User user) {
        if (actionedUser.isPublic()) {
            if (!user.getFollowings().contains(actionedUser))
                user.getFollowings().add(actionedUser);
            if (!actionedUser.getFollowers().contains(user))
                actionedUser.getFollowers().add(user);
        } else {
            return followRequest(actionedUser, user);
        }
        MainSaver.get().User.save(actionedUser);
        MainSaver.get().User.save(user);
        return new ShowProfileResponse(actionedUser, user);
    }

    public Response followRequest(User actionedUser, User user) {
        if (!actionedUser.getRequests().contains(user.getUsername()))
            actionedUser.getRequests().add(user.getUsername());
        MainSaver.get().User.save(actionedUser);
        MainSaver.get().User.save(user);
        return new ShowProfileResponse(actionedUser, user);
    }

    public Response updatePhoto(User user, String imageInString) {
        if (!imageInString.equals("")) {
            byte[] imageInBytes = Base64.getDecoder().decode(imageInString);
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInBytes), null);
            Images.save(image);
            user.setImageId(ID.getLastGeneratedID());
            user.setImageInString(imageInString);
            MainSaver.get().User.save(user);
        } else {
            user.setImageId(0);
            user.setImageInString("");
        }
        return new NoneResponse();
    }

    public Response unfollow(User actionedUser, User user) {
        user.getFollowings().remove(actionedUser);
        actionedUser.getFollowers().remove(user);
        actionedUser.getNotification().add(Notification.type2(user.getUsername()));
        MainSaver.get().User.save(user);
        MainSaver.get().User.save(actionedUser);
        return new ShowProfileResponse(actionedUser, user);

    }

    public Response unRequest(User actionedUser, User user) {
        actionedUser.getRequests().remove(user.getUsername());
        MainSaver.get().User.save(user);
        MainSaver.get().User.save(actionedUser);
        return new ShowProfileResponse(actionedUser, user);
    }

    public Response block(User actionedUser, User user) {
        actionedUser.getFollowings().remove(user);
        user.getFollowers().remove(actionedUser);
        user.getFollowings().remove(actionedUser);
        actionedUser.getFollowers().remove(user);
        actionedUser.getRequests().remove(user.getUsername());
        user.getRequests().remove(actionedUser.getUsername());
        if (!user.getBlocked().contains(actionedUser))
            user.getBlocked().add(actionedUser);
        MainSaver.get().User.save(actionedUser);
        MainSaver.get().User.save(user);
        return new ShowProfileResponse(actionedUser, user);
    }

    public Response unblock(User actionedUser, User user) {
        user.getBlocked().remove(actionedUser);
        MainSaver.get().User.save(user);
        MainSaver.get().User.save(actionedUser);
        return new ShowProfileResponse(actionedUser, user);
    }

    public Response mute(User actionedUser, User user) {
        if (!user.getMuted().contains(actionedUser))
            user.getMuted().add(actionedUser);
        MainSaver.get().User.save(user);
        return new ShowProfileResponse(actionedUser, user);

    }


    public Response unMute(User actionedUser, User user) {
        user.getMuted().remove(actionedUser);
        MainSaver.get().User.save(user);
        return new ShowProfileResponse(actionedUser, user);
    }

}
