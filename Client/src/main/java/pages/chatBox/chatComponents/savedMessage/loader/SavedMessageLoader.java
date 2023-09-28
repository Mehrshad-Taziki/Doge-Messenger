package pages.chatBox.chatComponents.savedMessage.loader;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import listener.RequestListener;
import logics.LogicalAgent;
import model.DB.DBMessage;
import model.DB.DBSavedMessage;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.chatBox.chatComponents.message.loader.MessageLoader;
import pages.chatBox.chatComponents.savedMessage.controller.SavedMessageController;
import tools.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SavedMessageLoader {
    private static final Logger logger = LogManager.getLogger(SavedMessageLoader.class);
    private final Config MESSAGE_CONFIG = Config.getConfig("Message");
    private final LogicalAgent logicalAgent;
    private final DBSavedMessage savedMessage;
    private final SavedMessageController controller;
    private final DBUser viewer;
    private int imageID;

    public SavedMessageLoader(LogicalAgent logicalAgent, DBSavedMessage savedMessage, DBUser viewer, SavedMessageController controller) {
        this.logicalAgent = logicalAgent;
        this.savedMessage = savedMessage;
        this.controller = controller;
        this.imageID = 0;
        this.viewer = viewer;
        loadMessageListener();
        loadMessagePane();
//        loadImageListener();
    }


    private void loadMessageListener() {
        controller.setMessageListener(new RequestListener(logicalAgent));
    }

    public void uploadImage(File file) {
//        try {
//            if (file != null) {
//                BufferedImage bufferedImage = ImageIO.read(file);
//                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
//                Images.save(image);
//                this.imageID = ID.getLastGeneratedID();
//            }
//            MainSaver.get().User.save(viewer);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void loadMessagePane() {
        ArrayList<AnchorPane> messages = new ArrayList<>();
        for (DBMessage message :
                savedMessage.getMessages()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource(MESSAGE_CONFIG.getProperty(String.class, "MessageComponent")));
                        AnchorPane anchorPane = fxmlLoader.load();
                        new MessageLoader(logicalAgent, message, viewer, fxmlLoader.getController(), savedMessage);
                        messages.add(anchorPane);
                    } catch (IOException e) {
                        logger.error("Loading Message " + message.getId() + " Failed");
                    }
                }
            });
        }
        controller.build(messages);
    }

    public void updateSavedMessage(ArrayList<DBMessage> messages) {
        if (hasNewMessage(messages)) {
            this.savedMessage.setMessages(messages);
            loadMessagePane();
        }
    }

    public boolean hasNewMessage(ArrayList<DBMessage> messages) {
        if (messages.size() == savedMessage.getMessages().size()) {
            for (int i = 0; i < savedMessage.getMessages().size(); i++) {
                if (!savedMessage.getMessages().get(i).getText().equals(messages.get(i).getText())) return true;
                if (!savedMessage.getMessages().get(i).getStatus().equals(messages.get(i).getStatus())) return true;
            }
        } else {
            return true;
        }
        return false;
    }
}
