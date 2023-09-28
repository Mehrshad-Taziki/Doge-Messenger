package responses;

import model.DB.DBSavedMessage;
import model.SavedMessage;

public class SavedMessageResponse extends Response {
    private final DBSavedMessage savedMessage;

    public SavedMessageResponse(SavedMessage savedMessage) {
        this.savedMessage = new DBSavedMessage(savedMessage);
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.showSavedMessage(savedMessage);
    }
}
