package model.DB;

import enums.SeenStatus;
import model.Message;

public class DBMessage {
    private final DBUser user;
    private final int id;
    private final SeenStatus status;
    private final boolean deleted;
    private final String text;
    private final String imageInString;

    public DBMessage(Message message) {
        this.user = new DBUser(message.getUser());
        this.text = message.getText().replaceAll("\\s+$", "");
        this.deleted = message.isDeleted();
        this.id = message.getId();
        this.status = message.getStatus();
        this.imageInString = message.getImageInString();
    }
    public DBMessage(DBUser user, String text, String imageInString) {
        this.user = user;
        this.text = text.replaceAll("\\s+$", "");
        this.deleted = false;
        this.id = 0;
        this.status = SeenStatus.NOTSENT;
        this.imageInString = imageInString;
    }

    public DBUser getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getImageInString() {
        return imageInString;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getId() {
        return id;
    }

    public SeenStatus getStatus() {
        return status;
    }
}
