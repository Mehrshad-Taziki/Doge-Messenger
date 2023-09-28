package listComponents.userPreview;


import logics.LogicalAgent;
import requests.SearchProfileRequest;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserPreview {
    private static final Logger logger = LogManager.getLogger(UserPreview.class);
    protected LogicalAgent logicalAgent;
    protected DBUser user;
    protected DBUser viewer;
    protected UserPreviewController controller;

    public UserPreview(LogicalAgent logicalAgent, DBUser user, DBUser viewer, UserPreviewController controller) {
        this.logicalAgent = logicalAgent;
        this.user = user;
        this.viewer = viewer;
        this.controller = controller;
        controller.build(user);
        loadNameListener();
    }

    protected void loadNameListener() {
        controller.setNameListener(username -> logicalAgent.addRequest(new SearchProfileRequest(username)));
    }
}
