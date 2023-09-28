package responses;

import model.Contact;
import model.DB.DBContact;

public class ContactResponse extends Response {
    private final DBContact contact;

    public ContactResponse(Contact contact) {
        this.contact = new DBContact(contact);
    }

    @Override
    public void takeAct(ResponseHandler responseHandler) {
        responseHandler.showContact(contact);
    }
}
