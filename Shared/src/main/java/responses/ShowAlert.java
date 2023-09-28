package responses;

import enums.AlertType;

public class ShowAlert extends Response {
    private final String message;
    private final AlertType type;

    public ShowAlert(String message, AlertType type) {
        this.message = message;
        this.type = type;
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.showAlert(message, type);
    }
}
