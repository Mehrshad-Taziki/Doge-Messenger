package offline;

import enums.AlertType;
import enums.ChatContainerType;
import enums.RequestAnswer;
import enums.SettingAction;
import logics.LogicalAgent;
import model.Notification;
import requests.*;
import responses.NoneResponse;
import responses.Response;
import responses.ShowAlert;
import tools.config.Alerts;

public class OfflineHandler implements RequestHandler {
    private static final OfflineDB dataBase = new OfflineDB();
    private final LogicalAgent logicalAgent;

    public OfflineHandler(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public Response updateChatList() {
        logicalAgent.updateChatLists(dataBase.getCurrentUserGroups(), dataBase.getCurrentUserPrivateChats(), dataBase.getCurrentUserContacts());
        return new NoneResponse();
    }

    @Override
    public Response searchContact(String contactName) {
        if (dataBase.getContacts().containsKey(contactName)) {

            logicalAgent.showContact(dataBase.getContacts().get(contactName));
            return new NoneResponse();
        }
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response searchPrivateChat(String username) {
        if (dataBase.getPrivateChats().containsKey(username)) {
            logicalAgent.showPrivateChat(dataBase.getPrivateChats().get(username));
            return new NoneResponse();
        }
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response searchGroup(String groupName) {
        if (dataBase.getGroups().containsKey(groupName)) {
            logicalAgent.showGroup(dataBase.getGroups().get(groupName));
            return new NoneResponse();
        }
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response chatBox() {
        if (dataBase.getCurrentUser() != null) {
            logicalAgent.chatBox(dataBase.getCurrentUser());
            return new NoneResponse();
        }
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response getSavedMessage() {
        if (dataBase.getSavedMessage() != null) {
            logicalAgent.showSavedMessage(dataBase.getSavedMessage());
            return new NoneResponse();
        }
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response editUserSetting(UserEditRequest request) {
        if (request != null) {
            int lastSeen = request.getLastSeenType();
            boolean publicEmail = request.isPublicEmail();
            boolean publicNumber = request.isPublicNumber();
            boolean publicBirthDay = request.isPublicBirthday();
            boolean isPublic = request.isPublic();
            dataBase.getCurrentUser().setLastSeenType(lastSeen);
            dataBase.getCurrentUser().setPublicEmail(publicEmail);
            dataBase.getCurrentUser().setPublicNumber(publicNumber);
            dataBase.getCurrentUser().setPublicBirthday(publicBirthDay);
            dataBase.getCurrentUser().setPublic(isPublic);
            OfflineDB.setRequest(request);
        }
        return new NoneResponse();
    }


    @Override
    public Response settingPage() {
        if (dataBase.getCurrentUser() != null) {
            logicalAgent.settingPage(dataBase.getCurrentUser());
            return new NoneResponse();
        }
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response sendMessage(MessageRequest request) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }


    @Override
    public Response save(SaveRequest request) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }


    @Override
    public Response updateChat() {
        return new NoneResponse();
    }

    //--------------------------------------------------------------------

    @Override
    public Response takePostAction(PostActionRequest postActionRequest) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response post(String s, String s1) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response commentOn(String s, int i) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response editProfile(EditProfileRequest editProfileRequest) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response search(String s) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response takeUserAction(UserActionRequest userActionRequest) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response takeSettingAction(SettingAction settingAction) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response deleteNotification(Notification notification) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response updatePersonalPage() {
        return new NoneResponse();
    }

    @Override
    public Response answer(String s, RequestAnswer requestAnswer) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response leaveGroup() {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response addToGroup(String s) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response addToContact(String s) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response editMessage(int i, String s) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response deleteMessage(int i) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response forward(String s, ChatContainerType chatContainerType, int i) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response register(RegisterRequest registerRequest) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response logout() {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response personalPage() {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response timeLinePage() {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response explorePage() {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }

    @Override
    public Response editProfilePhoto(String s) {
        return new ShowAlert(Alerts.offline(), AlertType.Error);
    }
}
