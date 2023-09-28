package requests;

import enums.ChatContainerType;
import enums.RequestAnswer;
import enums.SettingAction;
import model.Notification;
import responses.Response;

public interface RequestHandler {
    Response login(LoginRequest request);

    Response register(RegisterRequest request);

    Response logout();

    Response personalPage();

    Response timeLinePage();

    Response explorePage();

    Response settingPage();

    Response takePostAction(PostActionRequest request);

    Response post(String text, String imageInString);

    Response commentOn(String text, int motherPostID);

    Response editProfile(EditProfileRequest request);

    Response search(String username);

    Response takeUserAction(UserActionRequest request);

    Response takeSettingAction(SettingAction action);

    Response deleteNotification(Notification notification);

    Response updatePersonalPage();

    Response updateChatList();

    Response answer(String username, RequestAnswer answer);

    Response searchPrivateChat(String username);

    Response searchGroup(String name);

    Response sendMessage(MessageRequest messageRequest);

    Response updateChat();

    Response chatBox();

    Response leaveGroup();

    Response addToGroup(String username);

    Response addToContact(String username);

    Response searchContact(String name);

    Response getSavedMessage();

    Response save(SaveRequest request);

    Response editMessage(int id, String text);

    Response deleteMessage(int id);

    Response forward(String id, ChatContainerType type, int postID);

    Response editUserSetting(UserEditRequest request);

    Response editProfilePhoto(String imageInString);
}
