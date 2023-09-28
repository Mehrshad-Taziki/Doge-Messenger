package requests;

import responses.Response;

import java.time.LocalDate;

public class EditProfileRequest extends Request {
    private final String email;
    private final String name;
    private final String lastname;
    private final String biography;
    private final LocalDate birthday;
    private final boolean publicEmail;
    private final boolean publicNumber;
    private final boolean PublicBirthday;

    public EditProfileRequest(String email, String name, String lastname, String biography, LocalDate birthday, boolean publicEmail, boolean publicNumber, boolean publicBirthday) {
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.biography = biography;
        this.birthday = birthday;
        this.publicEmail = publicEmail;
        this.publicNumber = publicNumber;
        PublicBirthday = publicBirthday;
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

    public String getBiography() {
        return biography;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public boolean isPublicEmail() {
        return publicEmail;
    }

    public boolean isPublicNumber() {
        return publicNumber;
    }

    public boolean isPublicBirthday() {
        return PublicBirthday;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.editProfile(this);
    }
}
