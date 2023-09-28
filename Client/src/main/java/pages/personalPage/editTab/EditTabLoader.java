package pages.personalPage.editTab;

import logics.LogicalAgent;
import listener.RequestListener;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditTabLoader {
    private static final Logger logger = LogManager.getLogger(EditTabLoader.class);
    private final LogicalAgent logicalAgent;
    private final EditTabController controller;
    private final DBUser viewer;

    public EditTabLoader(LogicalAgent logicalAgent, EditTabController controller, DBUser viewer) {
        this.logicalAgent = logicalAgent;
        this.controller = controller;
        this.viewer = viewer;
        controller.build(viewer);
        loadEditListener();
    }

    private void loadEditListener() {
        controller.loadListener(new RequestListener(logicalAgent));
    }


    public void rebuildController(DBUser user){
        controller.build(user);
    }

}
