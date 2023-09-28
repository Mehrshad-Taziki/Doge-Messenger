package responses;

import enums.AlertType;
import enums.ChatContainerType;
import model.DB.*;

import java.util.ArrayList;
import java.util.HashSet;

public interface ResponseHandler {
    void showAlert(String message, AlertType type);

    void signPlayer(int token, DBUser user);

    void logOut();

    void personalPage(DBUser user, ArrayList<DBUser> followers, ArrayList<DBUser> followings, ArrayList<DBUser> blacklist, ArrayList<DBUser> requests);

    void timeLinePage(DBUser user, ArrayList<DBPost> selectedPosts);

    void explorePage(DBUser user, ArrayList<DBPost> selectedPosts);

    void chatBox(DBUser user);

    void settingPage(DBUser user);

    void showPost(DBPost post);

    void updatePostList(ArrayList<DBPost> posts);

    void showComments(ArrayList<DBPost> comments, DBPost motherPost, DBUser viewer);

    void rebuildEdit(DBUser user);

    void showProfile(ShowProfileResponse response);

    void updatePersonalPage(DBUser user, ArrayList<DBUser> followers, ArrayList<DBUser> followings, ArrayList<DBUser> blacklist, ArrayList<DBUser> requests);

    void updateChatLists(HashSet<DBGroup> groups, HashSet<DBPrivateChat> privateChats, HashSet<DBContact> contacts);

    void showPrivateChat(DBPrivateChat privateChat);

    void showGroup(DBGroup group);

    void showContact(DBContact contact);

    void showSavedMessage(DBSavedMessage savedMessage);

    void updateChat(ChatContainerType type, ArrayList<DBMessage> messages);

    void none();

    void reset();
}
