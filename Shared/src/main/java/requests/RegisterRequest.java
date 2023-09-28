package requests;

import responses.Response;

public class RegisterRequest extends Request {
    private final String userName, password, email, name, lastname, phoneNumber;

    public RegisterRequest(String userName, String password,String email,String name,String lastname,String phoneNumber) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.register(this);
    }
}
