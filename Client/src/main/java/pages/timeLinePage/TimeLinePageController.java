package pages.timeLinePage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import listener.Listener;

public class TimeLinePageController {
    private Listener backListener;

    @FXML
    private GridPane mainPane;

    public void back() {
        backListener.listen();
    }

    public void loadPane(AnchorPane anchorPane) {
        Platform.runLater(() -> {
            mainPane.getChildren().clear();
            mainPane.add(anchorPane, 0, 0);
        });

    }

    public void setBackListener(Listener backListener) {
        this.backListener = backListener;
    }
}
