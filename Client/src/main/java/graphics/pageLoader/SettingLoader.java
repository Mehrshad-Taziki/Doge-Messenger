package graphics.pageLoader;


import listener.BackListener;
import listener.RequestListener;
import logics.LogicalAgent;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.SettingPage.Listener.UserSettingActionListener;
import pages.SettingPage.SettingPageController;


public class SettingLoader {
    private static final Logger logger = LogManager.getLogger(SettingLoader.class);
    private final LogicalAgent logicalAgent;
    private DBUser viewer;
    private SettingPageController controller;

    public SettingLoader(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;

    }

    public void loadPage(DBUser user, SettingPageController controller) {
        this.controller = controller;
        viewer = user;
        loadTabListeners();
        loadActionListeners();
        loadListener();
        controller.build(viewer);
    }

    private void loadTabListeners() {
        controller.setBackListener(new BackListener(logicalAgent));
    }

    private void loadListener() {
        controller.setListener(new RequestListener(logicalAgent));
    }

    private void loadActionListeners() {
        controller.setActionListener(new UserSettingActionListener(logicalAgent));
    }

}
