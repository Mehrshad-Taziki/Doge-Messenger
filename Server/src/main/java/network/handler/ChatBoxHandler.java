package network.handler;

import config.Alerts;
import config.Config;
import database.DBSet.Context;
import database.*;
import database.saver.MainSaver;
import enums.AlertType;
import enums.ChatContainerType;
import enums.SeenStatus;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.*;
import model.DB.DBGroup;
import model.DB.DBPrivateChat;
import model.DB.DBSavedMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responses.*;
import tools.ImageHandler;
import tools.TimeTask;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;

public class ChatBoxHandler {
    private static final Logger logger = LogManager.getLogger(ChatBoxHandler.class);
    private static final Config POST_CONFIG = Config.getConfig("posts");

    public static void createPrivateChat(User user1, User user2) {
        user1.getPrivateChats().put(user2.getUsername(), new PrivateChat(user1, user2));
        user2.getPrivateChats().put(user1.getUsername(), new PrivateChat(user2, user1));
        FileCreator.createChat(user1.getUsername(), user2.getUsername());
        MainSaver.get().PrivateChat.save(user1.getPrivateChats().get(user2.getUsername()));
        MainSaver.get().PrivateChat.save(user2.getPrivateChats().get(user1.getUsername()));
        logger.info(user1.getUsername() + "/" + user1.getId() + " Private Chat With " + user2.getUsername() + "/" + user2.getId() + "File Created");
    }

    public static void message(User user1, User user2, Message message) {
        user1.getPrivateChats().get(user2.getUsername()).getMessages().add(message);
        user2.getPrivateChats().get(user1.getUsername()).getMessages().add(message);
        user2.getPrivateChats().get(user1.getUsername()).newMessage();
        MainSaver.get().PrivateChat.save(user1.getPrivateChats().get(user2.getUsername()));
        MainSaver.get().PrivateChat.save(user2.getPrivateChats().get(user1.getUsername()));
    }

    public static void messageGroup(String text, int time, User user, ChatType chat, String imageInString) {
        if (time == 0) {
            Message message = new Message(
                    user, text, false, ID.generateNewID(), SeenStatus.SENT);
            if (!imageInString.equals("")) {
                byte[] imageInBytes = Base64.getDecoder().decode(imageInString);
                Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInBytes), null);
                Images.save(image);
                message.setImageID(ID.getLastGeneratedID());
                message.setImageInString(imageInString);
            }
            Group group = user.getGroupsByName().get(chat.getId());
            for (User messagedUser :
                    group.getUsers()) {
                messagedUser.getGroupsByName().get(group.getGroupName()).getMessages().add(message);
                if (!messagedUser.equals(user)) {
                    messagedUser.getGroupsByName().get(group.getGroupName()).newMessage();
                }
                MainSaver.get().Group.save(messagedUser.getGroupsByName().get(group.getGroupName()));
            }
        } else {
            TimeTask messageTask = new TimeTask(time, () -> messageGroup(text, user, chat.getId(), imageInString));
            messageTask.start();
        }
    }

    public static void messageGroup(String text, User user, String groupName, String imageInString) {
        Message message = new Message(
                user, text, false, ID.generateNewID(), SeenStatus.SENT);
        if (!imageInString.equals("")) {
            byte[] imageInBytes = Base64.getDecoder().decode(imageInString);
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInBytes), null);
            Images.save(image);
            message.setImageID(ID.getLastGeneratedID());
            message.setImageInString(imageInString);
        }
        Group group = user.getGroupsByName().get(groupName);
        for (User messagedUser :
                group.getUsers()) {
            messagedUser.getGroupsByName().get(group.getGroupName()).getMessages().add(message);
            if (!messagedUser.equals(user)) {
                messagedUser.getGroupsByName().get(group.getGroupName()).newMessage();
            }
            MainSaver.get().Group.save(messagedUser.getGroupsByName().get(group.getGroupName()));
        }

    }

    public Response savePost(Post post, User user) {
        String msg = POST_CONFIG.getProperty(String.class, "forwardedText")
                + " " + post.getWriter().getUsername() + "\n";
        msg += post.getText();
        if (post.getImageID() != 0) {
            byte[] imageInByte = ImageHandler.toByteArray(SwingFXUtils.fromFXImage(Images.get(post.getImageID()), null), "png");
            String imageInString = Base64.getEncoder().encodeToString(imageInByte);
            save(msg, imageInString, user);
        } else {
            save(msg, "", user);
        }
        return new NoneResponse();
    }

    public Response updateChatList(User user) {
        if (user != null) {
            return new UpdateChatListResponse(user);
        }
        return new NoneResponse();
    }

    public Response searchPrivateChat(String username, User user, ChatType chat) {
        if (!Users.userExists(username)) {
            logger.warn(user.getUsername() + "/" + user.getId() + " Tried to Send Message To A Non Existing User");
            return new ShowAlert(Alerts.userNotExist(), AlertType.Error);
        } else if (!canSendMsg(username, user)) {
            return new ShowAlert(Alerts.cantSendMessage(), AlertType.Error);
        } else {
            if (!user.getPrivateChats().containsKey(username)) {
                User messagedUser = Context.get().Users.get(username);
                createPrivateChat(user, messagedUser);
            }
            user.getPrivateChats().get(username).setUnreadMessages(0);
            MainSaver.get().PrivateChat.save(user.getPrivateChats().get(username));
            chat.setId(username);
            chat.setType(ChatContainerType.PRIVATECHAT);
            return new PrivateChatResponse(user.getPrivateChats().get(username));
        }
    }

    public Response searchGroup(String name, User user, ChatType chat) {
        if (!user.getGroupsByName().containsKey(name)) {
            HashSet<User> members = new HashSet<>();
            members.add(user);
            int groupID = ID.generateNewID();
            user.getGroups().put(groupID, new Group(name, user, groupID, members));
            FileCreator.createGroup(user.getUsername(), name);
        }
        user.getGroupsByName().get(name).setUnreadMessages(0);
        MainSaver.get().Group.save(user.getGroupsByName().get(name));
        chat.setType(ChatContainerType.GROUP);
        chat.setId(name);
        return new GroupResponse(user.getGroupsByName().get(name));
    }

    public Response updateChat(User user, ChatType chat) {
        switch (chat.getType()) {
            case NONE, CONTACT -> {
                return new UpdateChatResponse(chat.getType(), new ArrayList<>());
            }
            case GROUP -> {
                if (user.getGroupsByName().containsKey(chat.getId())) {
                    return new UpdateChatResponse(chat.getType(), new DBGroup(user.getGroupsByName().get(chat.getId())).getMessages());
                }
            }
            case PRIVATECHAT -> {
                User messagedUser = Context.get().Users.get(chat.getId());
                if (messagedUser != null && user.getPrivateChats().containsKey(chat.getId())) {
                    messagedUser.getPrivateChats().get(user.getUsername()).seenMessages(SeenStatus.SEEN);
                    MainSaver.get().PrivateChat.save(messagedUser.getPrivateChats().get(user.getUsername()));
                    return new UpdateChatResponse(chat.getType(), new DBPrivateChat(user.getPrivateChats().get(chat.getId())).getMessages());
                }
            }
            case SAVEDMESSAGE -> {
                if (user.getSavedMessage() != null) {
                    return new UpdateChatResponse(chat.getType(), new DBSavedMessage(user.getSavedMessage()).getMessages());
                }
            }
        }
        return new NoneResponse();
    }

    public Response leaveGroup(User user, ChatType chat) {
        if (chat.getType() == ChatContainerType.GROUP) {
            Group group = user.getGroupsByName().get(chat.getId());
            user.getGroups().remove(group.getID());
            group.getUsers().remove(user);
            group.getUsers().forEach(groupUser -> groupUser.getGroupsByName().get(group.getGroupName()).getUsers().remove(user));
            group.getUsers().forEach(groupUser -> MainSaver.get().Group.save(groupUser.getGroupsByName().get(group.getGroupName())));
            Deleter.deleteGroupFile(user.getUsername(), group.getGroupName());
            chat.setType(ChatContainerType.NONE);
            chat.setId("");
        }
        return new NoneResponse();
    }

    public Response addToGroup(String username, User user, ChatType chat) {
        if (chat.getType() == ChatContainerType.GROUP) {
            Group group = user.getGroupsByName().get(chat.getId());
            if (!Users.userExists(username)) {
                logger.warn(user.getUsername() + "/" + user.getId() + " Tried to Add A Non Existing User");
                return new ShowAlert(Alerts.userNotExist(), AlertType.Error);
            } else if (!canAddToGroup(username, group.getGroupName(), user)) {
                return new ShowAlert(Alerts.cantAddToGroups(), AlertType.Error);
            } else if (group.getUsers().contains(Context.get().Users.get(username))) {
                return new ShowAlert(Alerts.alreadyInGroup(), AlertType.Error);
            } else {
                HashSet<User> users = new HashSet<>(group.getUsers());
                User newUser = Context.get().Users.get(username);
                users.forEach(groupUser -> groupUser.getGroupsByName().get(group.getGroupName()).getUsers().add(newUser));
                newUser.getGroups().put(group.getID(), new Group(group.getGroupName(), newUser, group.getID(), group.getUsers()));
                group.getMessages().forEach(message -> newUser.getGroups().get(group.getID()).getMessages().add(message));
                FileCreator.createGroup(newUser.getUsername(), group.getGroupName());
                group.getUsers().forEach(groupUser -> MainSaver.get().Group.save(groupUser.getGroupsByName().get(group.getGroupName())));
            }
        }
        return new NoneResponse();
    }

    public Response addToContact(String username, User user, ChatType chat) {
        if (chat.getType() == ChatContainerType.CONTACT) {
            Contact contact = user.getContacts().get(chat.getId());
            if (!Users.userExists(username)) {
                logger.info(user.getUsername() + "/" + user.getId() + " Tried to Add A Non Existing User");
                return new ShowAlert(Alerts.userNotExist(), AlertType.Error);
            } else if (!canSendMsg(username, user)) {
                return new ShowAlert(Alerts.cantAddToContacts(), AlertType.Error);
            } else if (contact.getMembers().contains(Context.get().Users.get(username))) {
                return new ShowAlert(Alerts.alreadyInContact(), AlertType.Error);
            } else {
                User newUser = Context.get().Users.get(username);
                contact.getMembers().add(newUser);
                MainSaver.get().User.save(user);
            }
        }
        return new NoneResponse();
    }

    public Response searchContact(String contactName, User user, ChatType chat) {
        if (contactName.equals("MessageAll")) {
            HashSet<User> allUsers = new HashSet<>();
            allUsers.addAll(user.getFollowers());
            allUsers.addAll(user.getFollowings());
            chat.setType(ChatContainerType.CONTACT);
            chat.setId(contactName);
            return new ContactResponse(new Contact("MessageAll", user, allUsers));
        }
        if (!user.getContacts().containsKey(contactName)) {
            HashSet<User> members = new HashSet<>();
            user.getContacts().put(contactName, new Contact(contactName, user, members));
            MainSaver.get().User.save(user);
        }
        chat.setType(ChatContainerType.CONTACT);
        chat.setId(contactName);
        return new ContactResponse(user.getContacts().get(contactName));
    }

    public Response getSavedMessage(User user, ChatType chat) {
        chat.setId("");
        chat.setType(ChatContainerType.SAVEDMESSAGE);
        return new SavedMessageResponse(user.getSavedMessage());
    }

    public Response save(String text, String imageInString, User user) {
        Message message = new Message(
                user, text, false, ID.generateNewID(), SeenStatus.SENT);
        if (!imageInString.equals("")) {
            byte[] imageInBytes = Base64.getDecoder().decode(imageInString);
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInBytes), null);
            Images.save(image);
            message.setImageID(ID.getLastGeneratedID());
            message.setImageInString(imageInString);
        }
        user.getSavedMessage().getMessages().add(message);
        MainSaver.get().SavedMessage.save(user.getSavedMessage());
        return new NoneResponse();
    }

    public Response editMessage(int id, String text, User user, ChatType chat) {
        switch (chat.getType()) {
            case PRIVATECHAT -> {
                user.getPrivateChats().get(chat.getId()).getViewerUser().getPrivateChats().get(chat.getId()).editMessage(id, text);
                user.getPrivateChats().get(chat.getId()).getSecondUser().getPrivateChats().get(chat.getId()).editMessage(id, text);
                MainSaver.get().User.save(user.getPrivateChats().get(chat.getId()).getSecondUser());
                MainSaver.get().User.save(user.getPrivateChats().get(chat.getId()).getViewerUser());
            }
            case GROUP -> {
                user.getGroupsByName().get(chat.getId()).getUsers().forEach(
                        groupUser -> groupUser.getGroupsByName().get(chat.getId()).editMessage(id, text));
                user.getGroupsByName().get(chat.getId()).getUsers().forEach(
                        groupUser -> MainSaver.get().User.save(groupUser));
            }
            case SAVEDMESSAGE -> {
                user.getSavedMessage().editMessage(id, text);
                MainSaver.get().SavedMessage.save(user.getSavedMessage());
            }
        }
        return new NoneResponse();
    }

    public Response deleteMessage(int id, User user, ChatType chat) {
        System.out.println("1");
        switch (chat.getType()) {
            case PRIVATECHAT -> {
                user.getPrivateChats().get(chat.getId()).getViewerUser().getPrivateChats().get(chat.getId()).deleteMessage(id);
                user.getPrivateChats().get(chat.getId()).getSecondUser().getPrivateChats().get(user.getUsername()).deleteMessage(id);
                MainSaver.get().User.save(user.getPrivateChats().get(chat.getId()).getSecondUser());
                MainSaver.get().User.save(user.getPrivateChats().get(chat.getId()).getViewerUser());
            }
            case GROUP -> {
                user.getGroupsByName().get(chat.getId()).getUsers().forEach(
                        groupUser -> groupUser.getGroupsByName().get(chat.getId()).deleteMessage(id));
                user.getGroupsByName().get(chat.getId()).getUsers().forEach(
                        groupUser -> MainSaver.get().User.save(groupUser));
            }
            case SAVEDMESSAGE -> {
                user.getSavedMessage().deleteMessage(id);
                MainSaver.get().SavedMessage.save(user.getSavedMessage());
            }
        }
        return new NoneResponse();
    }

    public Response forward(String id, ChatContainerType type, int postID, User user) {
        switch (type) {
            case PRIVATECHAT -> {
                if (!Users.userExists(id)) {
                    return new ShowAlert(Alerts.userNotExist(), AlertType.Error);
                } else if (!canSendMsg(id, user)) {
                    return new ShowAlert(Alerts.cantSendMessage(), AlertType.Error);
                } else {
                    if (!user.getPrivateChats().containsKey(id)) {
                        User forwardedUser = Context.get().Users.get(id);
                        createPrivateChat(user, forwardedUser);
                    }
                    User user1 = user;
                    User user2 = Context.get().Users.get(id);
                    Post post = Context.get().Posts.get(String.valueOf(postID));
                    String msg = POST_CONFIG.getProperty(String.class, "forwardedText")
                            + " " + post.getWriter().getUsername() + "\n";
                    msg += post.getText();
                    Message message = new Message(user1, msg, true, ID.generateNewID(), SeenStatus.SENT);
                    message.setImageID(post.getImageID());
                    user1.getPrivateChats().get(id).getMessages().add(message);
                    user2.getPrivateChats().get(user1.getUsername()).getMessages().add(message);
                    user2.getPrivateChats().get(user1.getUsername()).newMessage();
                    MainSaver.get().PrivateChat.save(user1.getPrivateChats().get(user2.getUsername()));
                    MainSaver.get().PrivateChat.save(user2.getPrivateChats().get(user1.getUsername()));
                }
            }
            case GROUP -> {
                if (!user.getGroupsByName().containsKey(id)) {
                    return new ShowAlert(Alerts.userNotExist(), AlertType.Error);
                } else {
                    Group group = user.getGroupsByName().get(id);
                    Post post = Context.get().Posts.get(String.valueOf(postID));
                    String msg = POST_CONFIG.getProperty(String.class, "forwardedText")
                            + " " + post.getWriter().getUsername() + "\n";
                    msg += post.getText();
                    for (User groupUser :
                            group.getUsers()) {
                        Message message = new Message(user, msg, true, ID.generateNewID(), SeenStatus.SENT);
                        message.setImageID(post.getImageID());
                        groupUser.getGroupsByName().get(id).getMessages().add(message);
                        if (!groupUser.equals(user)) {
                            groupUser.getGroupsByName().get(id).newMessage();
                        }
                        MainSaver.get().Group.save(groupUser.getGroupsByName().get(group.getGroupName()));
                    }
                }
            }
        }
        return new NoneResponse();
    }

    public void messagePrivateChat(String text, User user, ChatType chat, String imageInString) {
        Message message = new Message(
                user, text, false, ID.generateNewID(), SeenStatus.SENT);
        if (!imageInString.equals("")) {
            byte[] imageInBytes = Base64.getDecoder().decode(imageInString);
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInBytes), null);
            Images.save(image);
            message.setImageID(ID.getLastGeneratedID());
            message.setImageInString(imageInString);
        }
        User user1 = Context.get().Users.get(chat.getId());
        message(user, user1, message);
    }

    public void messageContact(String text, User user, ChatType chat, String imageInString) {
        Message message = new Message(user, text, false, ID.generateNewID(), SeenStatus.SENT);
        if (!imageInString.equals("")) {
            byte[] imageInBytes = Base64.getDecoder().decode(imageInString);
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInBytes), null);
            Images.save(image);
            message.setImageID(ID.getLastGeneratedID());
            message.setImageInString(imageInString);
        }
        HashSet<User> allUsers = new HashSet<>();
        allUsers.addAll(user.getFollowers());
        allUsers.addAll(user.getFollowings());
        user.getContacts().put("MessageAll", new Contact("MessageAll", user, allUsers));
        if (chat.getType() == ChatContainerType.CONTACT) {
            Contact contact = user.getContacts().get(chat.getId());
            for (User messagedUser :
                    contact.getMembers()) {
                if (!user.getPrivateChats().containsKey(messagedUser.getUsername())) {
                    createPrivateChat(user, messagedUser);
                }
                message(user, messagedUser, message);
            }
        }
        user.getContacts().remove("MessageAll");
        MainSaver.get().User.save(user);
    }

    private boolean canAddToGroup(String username, String groupName, User user) {
        User addedUser = Context.get().Users.get(username);
        if (addedUser.getGroupsByName().containsKey(groupName)) {
            return false;
        }
        if (Users.ifFollowed(user, username)) {
            return true;
        } else return Users.ifFollowingYou(user, username);
    }

    private boolean canSendMsg(String username, User user) {
        if (Users.ifFollowed(user, username)) {
            return true;
        } else return Users.ifFollowingYou(user, username);
    }

    public Response sendMessage(String text, int time, User user, ChatType chat, String imageInString) {
        switch (chat.getType()) {
            case GROUP -> messageGroup(text, time, user, chat, imageInString);
            case CONTACT -> messageContact(text, user, chat, imageInString);
            case PRIVATECHAT -> messagePrivateChat(text, user, chat, imageInString);
        }
        return new NoneResponse();
    }


    public void fixPrivateChats(User user, ChatType chat) {
        if (user != null) {
            for (String username :
                    user.getPrivateChats().keySet()) {
                User messagedUser = Context.get().Users.get(username);
                if (messagedUser != null) {
                    messagedUser.getPrivateChats().get(user.getUsername()).seenMessages(SeenStatus.ONLINENOTSEEN);
                    MainSaver.get().PrivateChat.save(messagedUser.getPrivateChats().get(user.getUsername()));
                }
            }
        }
        if (user != null && chat != null) {
            switch (chat.getType()) {
                case GROUP -> {
                    user.getGroupsByName().get(chat.getId()).setUnreadMessages(0);
                    MainSaver.get().Group.save(user.getGroupsByName().get(chat.getId()));
                }
                case PRIVATECHAT -> {
                    user.getPrivateChats().get(chat.getId()).setUnreadMessages(0);
                    MainSaver.get().PrivateChat.save(user.getPrivateChats().get(chat.getId()));
                }
            }
        }
    }
}
