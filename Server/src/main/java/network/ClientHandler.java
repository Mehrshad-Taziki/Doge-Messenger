package network;

import config.Alerts;
import config.FPS;
import database.DBSet.Context;
import database.ID;
import database.saver.MainSaver;
import enums.*;
import model.*;
import model.DB.DBUser;
import network.handler.Handler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.*;
import responses.*;
import tools.Loop;
import tools.TimeTask;

import java.security.SecureRandom;

public class ClientHandler extends Thread implements RequestHandler {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private final Handler handler = new Handler();
    private final ResponseSender sender;
    private final ConnectionHub connectionHub;
    private final Loop updateSeenLoop;
    private final TimeTask userOfflineTask;
    private boolean online = true;
    private TimeTask updateLastSeen;
    private ChatType chat;
    private User user;
    private int token;

    public ClientHandler(ResponseSender sender, ConnectionHub connectionHub) {
        this.token = 0;
        this.sender = sender;
        this.connectionHub = connectionHub;
        this.chat = new ChatType();
        this.updateSeenLoop = new Loop(FPS.updateTPS(), () -> handler.chatBoxHandler().fixPrivateChats(user, chat));
        this.userOfflineTask = new TimeTask(FPS.turnOffTime(), this::logout);
        this.updateLastSeen = new TimeTask(FPS.turnOffTime(), this::updateLastSeen);
        this.userOfflineTask.start();
        this.updateSeenLoop.start();
        this.updateLastSeen.start();
    }


    public void run() {
        while (online) {
            try {
                Request request = sender.getRequest();
                if (request.getToken() == token) {
                    sender.sendResponse(request.takeAct(this));
                    userOfflineTask.setTime(FPS.turnOffTime());
                } else sender.sendResponse(new NoneResponse());
            } catch (Throwable ignored) {
            }
        }
        if (user != null) logger.info(user.getUsername() + "/" + user.getId() + " Thread Closed");
        user = null;
        updateSeenLoop.stop();
        sender.close();
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        String username = loginRequest.getUserName();
//        if (connectionHub.getOnlineUsers().contains(username)) {
//            return new ShowAlert(Alerts.online(), AlertType.Info);
//        }
        if (handler.authHandler().isValid(loginRequest)) {
            ConnectionHub.getAllUsers().save();
            this.user = Context.get().Users.get(username);
            this.user.setActive(true);
            MainSaver.get().User.save(this.user);
            connectionHub.getOnlineUsers().add(username);
            SecureRandom random = new SecureRandom();
            this.token = random.nextInt();
            logger.info(user.getUsername() + "/" + user.getId() + " Logged In");
            return new SignPlayer(token, new DBUser(user));
        }
        return new ShowAlert(Alerts.password(), AlertType.Error);
    }

    @Override
    public Response register(RegisterRequest registerRequest) {
        String username = registerRequest.getUserName();
        if (handler.authHandler().isValid(registerRequest)) {
            User.UserBuilder builder = new User.UserBuilder();
            User helper = builder.setUserName(registerRequest.getUserName())
                    .setPassWord(registerRequest.getPassword())
                    .setEmail(registerRequest.getEmail())
                    .setPhoneNumber(registerRequest.getPhoneNumber())
                    .setLastName(registerRequest.getLastname())
                    .setName(registerRequest.getName())
                    .setId(ID.generateNewID()).build();
            ConnectionHub.getAllUsers().add(helper.getId(), helper.getUsername());
            ConnectionHub.getAllUsers().save();
            this.user = helper;
            MainSaver.get().User.save(helper);
            connectionHub.getOnlineUsers().add(username);
            ConnectionHub.getAllUsers().add(helper.getId(), helper.getUsername());
            ConnectionHub.getAllUsers().save();
            SecureRandom random = new SecureRandom();
            this.token = random.nextInt();
            logger.info(user.getUsername() + "/" + user.getId() + " Registered");
            return new SignPlayer(token, new DBUser(user));
        }
        return handler.authHandler().getRegisterAlert(registerRequest);
    }

    @Override
    public Response logout() {
        if (user != null) {
            connectionHub.getOnlineUsers().remove(user.getUsername());
            System.out.println(connectionHub.getOnlineUsers().contains(user.getUsername()));
            new TimeTask(FPS.updateTPS(), () -> this.online = false).start();
            logger.info(user.getUsername() + "/" + user.getId() + " Logged Out");
        }
        return new LogOutResponse();
    }

    @Override
    public Response personalPage() {
        return handler.personalHandler().personalPage(user);
    }

    @Override
    public Response timeLinePage() {
        return handler.timeLineHandler().timeLinePage(user);
    }

    @Override
    public Response explorePage() {
        return handler.exploreHandler().explorePage(user);
    }

    @Override
    public Response settingPage() {
        return handler.settingHandler().settingPage(user);
    }

    @Override
    public Response takePostAction(PostActionRequest postActionRequest) {
        Post post = Context.get().Posts.get(String.valueOf(postActionRequest.getID()));
        PostActions action = postActionRequest.getAction();
        switch (action) {
            case LIKE -> {
                return handler.postHandler().like(post, user);
            }
            case UNLIKE -> {
                return handler.postHandler().unLike(post, user);
            }
            case SAVE -> {
                return handler.chatBoxHandler().savePost(post, user);
            }
            case SEECOMMENTS -> {
                return handler.postHandler().seeComments(post, user);
            }
            case REPORT -> {
                return handler.postHandler().report(post, user);
            }
            case REPOST -> {
                return handler.postHandler().rePost(post, user);
            }
        }
        return new NoneResponse();
    }

    @Override
    public Response post(String text, String imageInString) {
        return handler.postHandler().post(text, imageInString, user);
    }

    @Override
    public Response commentOn(String text, int motherPostID) {
        return handler.postHandler().commentOn(text, motherPostID, user);
    }

    @Override
    public Response editProfile(EditProfileRequest request) {
        return handler.personalHandler().editProfile(request, user);
    }

    @Override
    public Response search(String name) {
        return handler.exploreHandler().search(name, user);
    }

    @Override
    public Response takeUserAction(UserActionRequest request) {
        User actionedUser = Context.get().Users.get(request.getUsername());
        UserAction action = request.getAction();
        switch (action) {
            case FOLLOW -> {
                return handler.profileHandler().follow(actionedUser, user);
            }
            case UNFOLLOW -> {
                return handler.profileHandler().unfollow(actionedUser, user);
            }
            case UNREQUEST -> {
                return handler.profileHandler().unRequest(actionedUser, user);
            }
            case BLOCK -> {
                return handler.profileHandler().block(actionedUser, user);
            }
            case UNBLOCK -> {
                return handler.profileHandler().unblock(actionedUser, user);
            }
            case MUTE -> {
                return handler.profileHandler().mute(actionedUser, user);
            }
            case UNMUTE -> {
                return handler.profileHandler().unMute(actionedUser, user);
            }
        }
        return new NoneResponse();
    }

    @Override
    public Response takeSettingAction(SettingAction action) {
        switch (action) {
            case DELETE -> {
                handler.settingHandler().delete(user);
                connectionHub.getOnlineUsers().remove(user.getUsername());
                ConnectionHub.getAllUsers().delete(user.getId());
                ConnectionHub.getAllUsers().save();
                this.user = null;
                this.chat = new ChatType();
                return new ResetResponse();
            }
            case LOGOUT -> {
                connectionHub.getOnlineUsers().remove(user.getUsername());
                this.user = null;
                this.chat = new ChatType();
                return handler.settingHandler().logOut();
            }
            case DEACTIVE -> {
                this.user.setActive(false);
                MainSaver.get().User.save(user);
                this.user = null;
                this.chat = new ChatType();
                return handler.settingHandler().deActive();
            }
        }
        return new NoneResponse();
    }

    @Override
    public Response deleteNotification(Notification notification) {
        return handler.personalHandler().deleteNotification(notification, user);
    }

    @Override
    public Response updatePersonalPage() {
        return handler.personalHandler().updatePersonalPage(user);
    }

    @Override
    public Response updateChatList() {
        return handler.chatBoxHandler().updateChatList(user);
    }

    @Override
    public Response answer(String username, RequestAnswer answer) {
        switch (answer) {
            case ACCEPT -> handler.profileHandler().accept(username, user);
            case DECLINE -> handler.profileHandler().decline(0, username, user);
            case HDECLINE -> handler.profileHandler().decline(1, username, user);
        }
        return new NoneResponse();
    }

    @Override
    public Response searchPrivateChat(String username) {
        return handler.chatBoxHandler().searchPrivateChat(username, user, chat);
    }

    @Override
    public Response searchGroup(String name) {
        return handler.chatBoxHandler().searchGroup(name, user, chat);
    }

    @Override
    public Response sendMessage(MessageRequest request) {
        return handler.chatBoxHandler().sendMessage(request.getText(), request.getTime(), user, chat, request.getImageInString());
    }

    @Override
    public Response updateChat() {
        return handler.chatBoxHandler().updateChat(user, chat);
    }

    @Override
    public Response chatBox() {
        return new ChatBoxResponse(user);
    }

    @Override
    public Response leaveGroup() {
        return handler.chatBoxHandler().leaveGroup(user, chat);
    }

    @Override
    public Response addToGroup(String username) {
        return handler.chatBoxHandler().addToGroup(username, user, chat);
    }

    @Override
    public Response addToContact(String username) {
        return handler.chatBoxHandler().addToContact(username, user, chat);
    }

    @Override
    public Response searchContact(String contactName) {
        return handler.chatBoxHandler().searchContact(contactName, user, chat);
    }

    @Override
    public Response getSavedMessage() {
        return handler.chatBoxHandler().getSavedMessage(user, chat);
    }

    @Override
    public Response save(SaveRequest request) {
        return handler.chatBoxHandler().save(request.getText(), request.getImageInString(), user);
    }

    @Override
    public Response editMessage(int id, String text) {
        return handler.chatBoxHandler().editMessage(id, text, user, chat);
    }

    @Override
    public Response deleteMessage(int id) {
        return handler.chatBoxHandler().deleteMessage(id, user, chat);
    }

    @Override
    public Response forward(String id, ChatContainerType type, int postID) {
        return handler.chatBoxHandler().forward(id, type, postID, user);
    }

    @Override
    public Response editUserSetting(UserEditRequest request) {
        return handler.settingHandler().editUserSetting(request, user);
    }

    @Override
    public Response editProfilePhoto(String imageInString) {
        return handler.profileHandler().updatePhoto(user, imageInString);
    }

    public void updateLastSeen() {
        this.updateLastSeen = new TimeTask(FPS.turnOffTime(), this::updateLastSeen);
        this.updateLastSeen.start();
        if (user != null) {
            user.setLastSeen(new DateTime());
            MainSaver.get().User.save(user);
        }
    }

}
