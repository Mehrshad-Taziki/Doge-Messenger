package logics;

import enums.AlertType;
import enums.ChatContainerType;
import graphics.GraphicalAgent;
import graphics.pageLoader.PageLoader;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DB.*;
import offline.OfflineDB;
import offline.OfflineHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.Request;
import requests.UpdateChatListRequest;
import requests.UpdateChatRequest;
import requests.UpdatePersonalPageRequest;
import responses.Response;
import responses.ResponseHandler;
import responses.ShowProfileResponse;
import sender.SocketRequestSender;
import tools.Loop;
import tools.TimeTask;
import tools.config.Alerts;
import tools.config.Config;
import tools.config.FPS;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class LogicalAgent implements ResponseHandler {
    private static final Logger logger = LogManager.getLogger(LogicalAgent.class);
    private final GraphicalAgent graphicalAgent;
    private final LinkedList<Request> requests;
    private final Loop loop;
    private final OfflineHandler offlineHandler;
    private final Loop updateChatList;
    private final Loop updateChat;
    private SocketRequestSender requestSender;
    private TimeTask updatePersonal;
    private int token = 0;
    private int stage = 0;

    public LogicalAgent(Socket socket, Stage mainStage) throws IOException {
        this.requestSender = new SocketRequestSender(socket);
        this.graphicalAgent = new GraphicalAgent(mainStage, this);
        this.requests = new LinkedList<>();
        this.loop = new Loop(FPS.sendRequestTPS(), this::sendRequests);
        this.updatePersonal = new TimeTask(FPS.updatePersonalFirst(), () -> addRequest(new UpdatePersonalPageRequest()));
        this.updateChatList = new Loop(FPS.updateTPS(), () -> addRequest(new UpdateChatListRequest()));
        this.updateChat = new Loop(FPS.updateTPS(), () -> addRequest(new UpdateChatRequest()));
        this.offlineHandler = new OfflineHandler(this);
    }

    public void reConnect() {
        try {
            this.token = 0;
            this.stage = 0;
            Config CONNECTION_CONFIG = Config.getConfig("connection");
            int port = CONNECTION_CONFIG.getProperty(Integer.class, "port");
            String ip = CONNECTION_CONFIG.getProperty(String.class, "ip");
            if (port == 0) port = 5555;
            if (ip == null) ip = "127.0.0.1";
            Socket socket = new Socket(ip, port);
            this.requestSender = new SocketRequestSender(socket);
            graphicalAgent.reset();
        } catch (Throwable throwable) {
            showAlert(Alerts.offline(), AlertType.Error);
        }

    }


    public void start() {
        graphicalAgent.start();
        loop.start();
        updateChat.start();
        updateChatList.start();
        updatePersonal.start();
    }

    public void addRequest(Request request) {
        synchronized (requests) {
            request.setToken(token);
            requests.add(request);
        }
    }

    public void sendRequests() {
        System.out.println(requests.size());
        LinkedList<Request> doneRequests;
        synchronized (requests) {
            doneRequests = new LinkedList<>(requests);
            requests.clear();
        }
        for (Request request :
                doneRequests) {
            try {
                Response serverResponse = requestSender.sendRequest(request);
                if (serverResponse != null) serverResponse.takeAct(this);
            } catch (Throwable throwable) {
                logger.info("User Did Action In Offline");
                Response offlineResponse = request.takeAct(offlineHandler);
                offlineResponse.takeAct(this);
            }
        }

    }

    public void switchToRegisterForm() {
        graphicalAgent.switchToRegisterForm();
    }

    public void switchToLoginForm() {
        graphicalAgent.switchToLoginForm();
    }

    public void switchToMainPage() {
        graphicalAgent.switchToMainPage();
    }

    public void goToPreviousPage() {
        graphicalAgent.goToPreviousPage();
    }

    public void setScene(Scene scene) {
        graphicalAgent.setScene(scene);
    }

    @Override
    public void showAlert(String message, AlertType type) {
        Platform.runLater(() -> {
            if (type == AlertType.Error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }
            if (type == AlertType.Info) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }
            if (type == AlertType.Question) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }
        });
    }

    @Override
    public void signPlayer(int token, DBUser user) {
        if (token != 0) {
            this.token = token;
        }
        if (OfflineDB.getRequest() != null) {
            addRequest(OfflineDB.getRequest());
        }
        OfflineDB.setCurrentUser(user);
        logger.info("User Logged In");
        switchToMainPage();
    }


    @Override
    public void logOut() {
        logger.info("User Logged Out");
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void personalPage(DBUser user, ArrayList<DBUser> followers, ArrayList<DBUser> followings, ArrayList<DBUser> blacklist, ArrayList<DBUser> requests) {
        graphicalAgent.switchToPersonalPage(user, followers, followings, blacklist, requests);
    }


    @Override
    public void timeLinePage(DBUser user, ArrayList<DBPost> selectedPost) {
        graphicalAgent.switchToTimeLinePage(user, selectedPost);
    }

    @Override
    public void explorePage(DBUser user, ArrayList<DBPost> selectedPosts) {
        graphicalAgent.switchToExplorePage(user, selectedPosts);
    }

    @Override
    public void chatBox(DBUser user) {
        graphicalAgent.switchToChatBox(user);
    }

    @Override
    public void settingPage(DBUser user) {
        graphicalAgent.switchToSetting(user);
    }

    @Override
    public void showPost(DBPost post) {
        if (stage == 1)
            PageLoader.get().getPersonalPageLoader().updatePost(post);
        if (stage == 2)
            PageLoader.get().getTimeLinePageLoader().updatePost(post);
        if (stage == 3)
            PageLoader.get().getExplorePageLoader().updatePost(post);
    }

    @Override
    public void updatePostList(ArrayList<DBPost> posts) {
        if (stage == 1) {
            PageLoader.get().getPersonalPageLoader().updatePosts(posts);
        }
    }

    @Override
    public void showComments(ArrayList<DBPost> comments, DBPost motherPost, DBUser viewer) {
        graphicalAgent.showComments(comments, motherPost, viewer);
    }

    @Override
    public void rebuildEdit(DBUser user) {
        PageLoader.get().getPersonalPageLoader().rebuildEdit(user);
    }

    @Override
    public void showProfile(ShowProfileResponse response) {
        if (stage == 3) {
            PageLoader.get().getExplorePageLoader().buildProfile(response);
        } else {
            PageLoader.get().getPersonalPageLoader().showProfile(response);
        }
    }

    @Override
    public void updatePersonalPage(DBUser user, ArrayList<DBUser> followers, ArrayList<DBUser> followings, ArrayList<DBUser> blacklist, ArrayList<DBUser> requests) {
        PageLoader.get().getPersonalPageLoader().updateLists(followers, followings, blacklist, requests);
        this.updatePersonal = new TimeTask(FPS.updatePersonalSecond(), () -> addRequest(new UpdatePersonalPageRequest()));
        updatePersonal.start();
    }

    @Override
    public void updateChatLists(HashSet<DBGroup> groups, HashSet<DBPrivateChat> privateChats, HashSet<DBContact> contacts) {
        OfflineDB.setCurrentUserContacts(contacts);
        OfflineDB.setCurrentUserGroups(groups);
        OfflineDB.setCurrentUserPrivateChats(privateChats);
        PageLoader.get().getChatBoxLoader().loadMessagePreviewList(groups, privateChats, contacts);
    }

    @Override
    public void showPrivateChat(DBPrivateChat privateChat) {
        OfflineDB.save(privateChat);
        PageLoader.get().getChatBoxLoader().loadPrivateChatPane(privateChat);
    }

    @Override
    public void showGroup(DBGroup group) {
        OfflineDB.save(group);
        PageLoader.get().getChatBoxLoader().loadGroupChatPane(group);
    }

    @Override
    public void showContact(DBContact contact) {
        OfflineDB.save(contact);
        PageLoader.get().getChatBoxLoader().loadContactChatPane(contact);
    }

    @Override
    public void showSavedMessage(DBSavedMessage savedMessage) {
        OfflineDB.save(savedMessage);
        PageLoader.get().getChatBoxLoader().loadPrivateChatPane(savedMessage);
    }

    @Override
    public void updateChat(ChatContainerType type, ArrayList<DBMessage> messages) {
        PageLoader.get().getChatBoxLoader().updateChat(type, messages);
    }

    @Override
    public void none() {
    }

    @Override
    public void reset() {
        OfflineDB.reset();
        graphicalAgent.reset();
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

}
