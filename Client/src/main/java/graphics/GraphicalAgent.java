package graphics;

import authentication.controller.LoginFormController;
import authentication.controller.RegisterFormController;
import authentication.listener.AuthExitListener;
import authentication.listener.LoginListener;
import authentication.listener.RegisterListener;
import graphics.pageLoader.PageLoader;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import listener.BackListener;
import listener.RequestListener;
import logics.LogicalAgent;
import media.comment.logic.CommentLogic;
import model.DB.DBPost;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.explorePage.ExplorePageController;
import pages.mainPage.MainPageController;
import pages.mainPage.listener.MainPageListener;
import pages.personalPage.PersonalPageController;
import pages.timeLinePage.TimeLinePageController;
import pages.welcomePage.WelcomePageController;
import pages.welcomePage.listener.WelcomePageListener;
import requests.LogOutRequest;
import tools.config.Config;
import tools.config.FrameStats;
import tools.config.PageAddresses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class GraphicalAgent {
    private static final Logger logger = LogManager.getLogger(GraphicalAgent.class);
    private final Stage mainStage;
    private final Stack<Scene> scenes;
    private final LogicalAgent logicalAgent;

    public GraphicalAgent(Stage mainStage, LogicalAgent logicalAgent) {
        scenes = new Stack<>();
        this.mainStage = mainStage;
        this.logicalAgent = logicalAgent;
        new PageLoader(logicalAgent);
    }

    public void start() {
        try {
            mainStage.setTitle(FrameStats.title());
            mainStage.setResizable(FrameStats.resizable());
            mainStage.setMinHeight(FrameStats.height());
            mainStage.setMinWidth(FrameStats.width());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PageAddresses.welcome()));
            Parent root = fxmlLoader.load();
            ((WelcomePageController) fxmlLoader.getController()).setPageListener(new WelcomePageListener(logicalAgent));
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.setOnCloseRequest(t -> logicalAgent.addRequest(new LogOutRequest()));
            mainStage.show();
        } catch (Exception e) {
            logger.error("Staring Main Frame Failed " + e.getMessage());
        }
    }

    public void switchToRegisterForm() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PageAddresses.register()));
                    Parent root = fxmlLoader.load();
                    ((RegisterFormController) fxmlLoader.getController()).setFormListener(new RegisterListener(logicalAgent));
                    ((RegisterFormController) fxmlLoader.getController()).setActionListener(new AuthExitListener(logicalAgent));
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                } catch (IOException e) {
                    logger.error("Loading Registration Form Failed " + e.getMessage());
                }
            }
        });

    }

    public void switchToLoginForm() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PageAddresses.login()));
                    Parent root = fxmlLoader.load();
                    ((LoginFormController) fxmlLoader.getController()).setFormListener(new LoginListener(logicalAgent));
                    ((LoginFormController) fxmlLoader.getController()).setActionListener(new AuthExitListener(logicalAgent));
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                } catch (IOException e) {
                    logger.error("Loading Login Form Failed " + e.getMessage());
                }
            }
        });

    }

    public void switchToMainPage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PageAddresses.main()));
                    Parent root = fxmlLoader.load();
                    ((MainPageController) fxmlLoader.getController()).setPageListener(new MainPageListener(logicalAgent));
                    ((MainPageController) fxmlLoader.getController()).setListener(() -> logicalAgent.addRequest(new LogOutRequest()));
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                    scenes.add(scene);
                } catch (IOException e) {
                    logger.error("Loading Main Page Failed " + e.getMessage());
                }
            }
        });
    }

    public void setScene(Scene scene) {
        mainStage.setScene(scene);
        scenes.add(scene);
    }

    public void reset() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    scenes.clear();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PageAddresses.welcome()));
                    Parent root = fxmlLoader.load();
                    ((WelcomePageController) fxmlLoader.getController()).setPageListener(new WelcomePageListener(logicalAgent));
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                } catch (IOException e) {
                    logger.error("Loading Welcome Page Failed " + e.getMessage());
                }
            }
        });
    }

    public void goToPreviousPage() {
        PageLoader.get().getExplorePageLoader().stopLoop();
        scenes.pop();
        Platform.runLater(() -> mainStage.setScene(scenes.peek()));
        if (scenes.size() == 1) logicalAgent.setStage(0);
    }

    public void switchToPersonalPage(DBUser user, ArrayList<DBUser> followers, ArrayList<DBUser> followings, ArrayList<DBUser> blacklist, ArrayList<DBUser> requests) {
        logicalAgent.setStage(1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PageAddresses.personal()));
                    Parent root = fxmlLoader.load();
                    ((PersonalPageController) fxmlLoader.getController()).setPostListener(new RequestListener(logicalAgent));
                    ((PersonalPageController) fxmlLoader.getController()).setBackListener(new BackListener(logicalAgent));
                    PageLoader.get().getPersonalPageLoader().loadPage(user, fxmlLoader.getController(), followers, followings, blacklist, requests);
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                    scenes.add(scene);
                } catch (IOException e) {
                    logger.error("Loading Personal Page Failed " + e.getMessage());
                }
            }
        });
    }


    public void switchToTimeLinePage(DBUser user, ArrayList<DBPost> selectedPosts) {
        logicalAgent.setStage(2);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PageAddresses.timeline()));
                    Parent root = fxmlLoader.load();
                    ((TimeLinePageController) fxmlLoader.getController()).setBackListener(new BackListener(logicalAgent));
                    PageLoader.get().getTimeLinePageLoader().loadPage(user, fxmlLoader.getController(), selectedPosts);
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                    scenes.add(scene);
                } catch (IOException e) {
                    logger.error("Loading TimeLine Page Failed " + e.getMessage());
                }
            }
        });
    }

    public void switchToExplorePage(DBUser user, ArrayList<DBPost> selectedPosts) {
        logicalAgent.setStage(3);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PageAddresses.explore()));
                    Parent root = fxmlLoader.load();
                    ((ExplorePageController) fxmlLoader.getController()).setBackListener(new BackListener(logicalAgent));
                    ((ExplorePageController) fxmlLoader.getController()).setSearchListener(new RequestListener(logicalAgent));
                    PageLoader.get().getExplorePageLoader().loadPage(user, fxmlLoader.getController(), selectedPosts);
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                    scenes.add(scene);
                } catch (IOException e) {
                    logger.error("Loading Explore Page Failed " + e.getMessage());
                }
            }
        });
    }

    public void switchToSetting(DBUser user) {
        logicalAgent.setStage(5);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PageAddresses.setting()));
                    Parent root = fxmlLoader.load();
                    PageLoader.get().getSettingLoader().loadPage(user, fxmlLoader.getController());
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                    scenes.add(scene);
                } catch (Exception e) {
                    logger.error("Loading Setting Page Failed " + e.getMessage());
                }
            }
        });

    }

    public void switchToChatBox(DBUser user) {
        logicalAgent.setStage(4);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PageAddresses.chatBox()));
                    Parent root = fxmlLoader.load();
                    PageLoader.get().getChatBoxLoader().loadPage(user, fxmlLoader.getController());
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                    scenes.add(scene);
                } catch (IOException e) {
                    logger.error("Loading ChatBox Failed " + e.getMessage());
                }
            }
        });
    }

    public void showComments(ArrayList<DBPost> comments, DBPost motherPost, DBUser viewer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    Config POST_CONFIG = Config.getConfig("posts");
                    fxmlLoader.setLocation(getClass().getResource(POST_CONFIG.getProperty(String.class, "commentComponent")));
                    Parent root = fxmlLoader.load();
                    new CommentLogic(logicalAgent, viewer, fxmlLoader.getController(), comments, motherPost);
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                    scenes.add(scene);
                } catch (IOException e) {
                    logger.error("Loading Comments Of Post " + motherPost.getId() + " Failed : " + e.getMessage());
                }
            }
        });
    }
}

