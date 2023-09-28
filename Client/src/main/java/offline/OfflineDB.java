package offline;

import model.DB.*;
import requests.Request;

import java.util.HashMap;
import java.util.HashSet;

public class OfflineDB {
    private static final HashMap<String, DBPrivateChat> privateChats = new HashMap<>();
    private static final HashMap<String, DBGroup> groups = new HashMap<>();
    private static final HashMap<String, DBContact> contacts = new HashMap<>();
    private static HashSet<DBGroup> currentUserGroups = new HashSet<>();
    private static HashSet<DBPrivateChat> currentUserPrivateChats = new HashSet<>();
    private static HashSet<DBContact> currentUserContacts = new HashSet<>();
    private static DBSavedMessage savedMessage;
    private static DBUser currentUser;
    private static Request request;

    public static void save(DBSavedMessage savedMessage) {
        OfflineDB.savedMessage = savedMessage;
    }

    public static void save(DBPrivateChat privateChat) {
        privateChats.put(privateChat.getSecondUser().getUsername(), privateChat);
    }

    public static void save(DBContact contact) {
        contacts.put(contact.getContactName(), contact);
    }

    public static void save(DBGroup group) {
        groups.put(group.getGroupName(), group);
    }

    public static void reset() {
        privateChats.clear();
        contacts.clear();
        groups.clear();
        currentUserContacts.clear();
        currentUserGroups.clear();
        currentUserPrivateChats.clear();
        request = null;
    }

    public static Request getRequest() {
        return request;
    }

    public static void setRequest(Request request) {
        OfflineDB.request = request;
    }

    public HashSet<DBGroup> getCurrentUserGroups() {
        return currentUserGroups;
    }

    public static void setCurrentUserGroups(HashSet<DBGroup> currentUserGroups) {
        OfflineDB.currentUserGroups = currentUserGroups;
    }

    public HashSet<DBPrivateChat> getCurrentUserPrivateChats() {
        return currentUserPrivateChats;
    }

    public static void setCurrentUserPrivateChats(HashSet<DBPrivateChat> currentUserPrivateChats) {
        OfflineDB.currentUserPrivateChats = currentUserPrivateChats;
    }

    public HashSet<DBContact> getCurrentUserContacts() {
        return currentUserContacts;
    }

    public static void setCurrentUserContacts(HashSet<DBContact> currentUserContacts) {
        OfflineDB.currentUserContacts = currentUserContacts;
    }

    public DBUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(DBUser user) {
        currentUser = user;
    }

    public DBSavedMessage getSavedMessage() {
        return savedMessage;
    }

    public HashMap<String, DBPrivateChat> getPrivateChats() {
        return privateChats;
    }

    public HashMap<String, DBGroup> getGroups() {
        return groups;
    }

    public HashMap<String, DBContact> getContacts() {
        return contacts;
    }
}
