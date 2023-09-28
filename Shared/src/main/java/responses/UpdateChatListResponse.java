package responses;

import model.DB.DBContact;
import model.DB.DBGroup;
import model.DB.DBPrivateChat;
import model.User;

import java.util.HashSet;

public class UpdateChatListResponse extends Response {
    private final HashSet<DBGroup> groups;
    private final HashSet<DBContact> contacts;
    private final HashSet<DBPrivateChat> privateChats;

    public UpdateChatListResponse(User user) {
        this.groups = new HashSet<>();
        this.contacts = new HashSet<>();
        this.privateChats = new HashSet<>();
        user.getGroups().values().forEach(group -> this.groups.add(new DBGroup(group)));
        user.getPrivateChats().values().forEach(privateChat -> this.privateChats.add(new DBPrivateChat(privateChat)));
        user.getContacts().values().forEach(contact -> this.contacts.add(new DBContact(contact)));
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.updateChatLists(groups, privateChats, contacts);
    }
}
