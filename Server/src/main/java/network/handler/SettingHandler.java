package network.handler;

import config.Alerts;
import database.Deleter;
import database.Validation;
import database.saver.MainSaver;
import enums.AlertType;
import model.Group;
import model.Post;
import model.PrivateChat;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.UserEditRequest;
import responses.*;

import java.util.ArrayList;

public class SettingHandler {
    private static final Logger logger = LogManager.getLogger(SettingHandler.class);

    public void delete(User user) {
        try {
            User deletedUser = user;
            for (User client :
                    deletedUser.getFollowings()) {
                client.getFollowers().remove(deletedUser);
                MainSaver.get().User.save(client);
            }
            for (User client :
                    deletedUser.getFollowers()) {
                client.getFollowings().remove(deletedUser);
                client.getFollowers().remove(deletedUser);
                client.getBlocked().remove(deletedUser);
                client.getContacts().forEach((string, contact) -> contact.getMembers().remove(deletedUser));
                client.getRequests().remove(deletedUser.getUsername());
                MainSaver.get().User.save(client);
            }
            for (Post post :
                    deletedUser.getLiked()) {
                post.getLikes().remove(deletedUser);
                MainSaver.get().Post.save(post);
            }
            for (Post post :
                    Post.loadedPosts) {
                if (post.getWriter().equals(deletedUser)) {
                    Deleter.deletePost(post);
                    MainSaver.get().User.save(post.getWriter());
                }
            }
            for (Group group :
                    user.getGroupsByName().values()) {
                group.getUsers().remove(user);
                group.getUsers().forEach(groupUser -> groupUser.getGroupsByName().get(group.getGroupName()).getUsers().remove(user));
                group.getUsers().forEach(groupUser -> MainSaver.get().Group.save(groupUser.getGroupsByName().get(group.getGroupName())));
                Deleter.deleteGroupFile(user.getUsername(), group.getGroupName());
                MainSaver.get().Group.save(group);
            }
            ArrayList<PrivateChat> deletedPrivateChat = new ArrayList<>(deletedUser.getPrivateChats().values());
            for (PrivateChat privateChat :
                deletedPrivateChat ) {
                User viewerUser = privateChat.getViewerUser();
                User secondUser = privateChat.getSecondUser();
                viewerUser.getPrivateChats().remove(secondUser.getUsername());
                secondUser.getPrivateChats().remove(viewerUser.getUsername());
                Deleter.deleteChatFile(viewerUser.getUsername(), secondUser.getUsername());
            }
            Deleter.deleteUser(deletedUser);
        } catch (Throwable throwable) {
            logger.error("Something Wrong Happened Deleting The User " + throwable.getMessage());
        }
    }

    public Response deActive() {
        return logOut();
    }

    public Response logOut() {
        return new ResetResponse();
    }

    public Response editUserSetting(UserEditRequest request, User user) {
        String password = request.getPassword();
        int lastSeen = request.getLastSeenType();
        boolean publicEmail = request.isPublicEmail();
        boolean publicNumber = request.isPublicNumber();
        boolean publicBirthDay = request.isPublicBirthday();
        boolean isPublic = request.isPublic();
        user.setLastSeenType(lastSeen);
        user.setPublicEmail(publicEmail);
        user.setPublicNumber(publicNumber);
        user.setPublicBirthday(publicBirthDay);
        user.setPublic(isPublic);
        MainSaver.get().User.save(user);
        if (!password.equals("") && Validation.isValidPass(password)) {
            user.setPassword(password);
            MainSaver.get().User.save(user);
        } else return new ShowAlert(Alerts.passwordNS(), AlertType.Error);
        return new NoneResponse();
    }

    public Response settingPage(User user) {
        return new SettingPageResponse(user);
    }

}
