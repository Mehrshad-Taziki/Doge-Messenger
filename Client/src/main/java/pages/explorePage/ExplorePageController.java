package pages.explorePage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import listener.Listener;
import listener.RequestListener;
import requests.SearchProfileRequest;

public class ExplorePageController {

    private RequestListener searchListener;
    private Listener backListener;
    @FXML
    private TextField searchTextField;
    @FXML
    private GridPane profilePane;
    @FXML
    private GridPane postPane;

    public void search() {
        if (searchTextField.getText().equals("")) return;
        searchListener.listen(new SearchProfileRequest(searchTextField.getText()));
    }

    public void setSearchListener(RequestListener searchListener) {
        this.searchListener = searchListener;
    }

    public void setBackListener(Listener backListener) {
        this.backListener = backListener;
    }

    public void back() {
        backListener.listen();
    }

    public void loadPostPane(AnchorPane anchorPane) {
        Platform.runLater(() -> {
            postPane.getChildren().clear();
            postPane.add(anchorPane, 0, 0);
        });
    }

    public void loadProfilePane(StackPane stackPane) {
        Platform.runLater(() -> {
            profilePane.getChildren().clear();
            profilePane.add(stackPane, 0, 0);
        });
    }

}
