package listComponents.userPreview;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import listComponents.userRequestPreview.listener.RequestListener;
import listener.StringListener;
import model.DB.DBUser;
import tools.ImageHandler;

import java.util.Base64;

public class UserPreviewController {

    protected RequestListener requestListener;

    protected StringListener nameListener;
    @FXML
    Label nameLabel;
    @FXML
    Label lastnameLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Circle profileImageView;

    public void build(DBUser user) {
        if (!user.getImageInString().equals("") && user.getImageInString() != null) {
            byte[] imageInByte = Base64.getDecoder().decode(user.getImageInString());
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
            profileImageView.setFill(new ImagePattern(image));
            profileImageView.setVisible(true);
        }
        nameLabel.setText(user.getName());
        lastnameLabel.setText(user.getLastName());
        usernameLabel.setText(user.getUsername());
    }

    public void setNameListener(StringListener nameListener) {
        this.nameListener = nameListener;
    }

    @FXML
    public void view() {
        nameListener.listen(usernameLabel.getText());
    }

    public void setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }
}
