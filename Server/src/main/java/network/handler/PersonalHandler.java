package network.handler;

import config.Alerts;
import database.DBSet.Context;
import database.Users;
import database.Validation;
import database.saver.MainSaver;
import enums.AlertType;
import model.DB.DBUser;
import model.DateTime;
import model.Notification;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.EditProfileRequest;
import responses.*;

import java.util.ArrayList;
import java.util.Objects;

public class PersonalHandler {
    private static final Logger logger = LogManager.getLogger(PersonalHandler.class);

    public Response personalPage(User user) {
        fixNotification(user);
        MainSaver.get().User.save(user);
        return new PersonalPageResponse(user, getRequests(user));
    }


    private void fixNotification(User user) {
        user.getNotification().removeIf(notification -> !Users.userExists(notification.getUsername()));
    }


    private ArrayList<DBUser> getRequests(User user) {
        ArrayList<DBUser> ans = new ArrayList<>();
        for (String username :
                user.getRequests()) {
            User requestedUser = Context.get().Users.get(username);
            if (requestedUser != null) {
                ans.add(new DBUser(requestedUser));
            }
        }
        return ans;
    }

    public Response updatePersonalPage(User user) {
        if (user != null) {
            fixNotification(user);
            MainSaver.get().User.save(user);
            return new UpdatePersonalPageResponse(user, getRequests(user));
        }
        logger.warn("User Is Null!");
        return new NoneResponse();
    }

    public Response deleteNotification(Notification notification, User user) {
        user.getNotification().remove(notification);
        MainSaver.get().User.save(user);
        return updatePersonalPage(user);
    }

    public Response editProfile(EditProfileRequest request, User user) {
        boolean validEmail = true;
        String email = request.getEmail();
        if (!Validation.isValidEmail(email) || !Validation.isAvailableEMail(email)) {
            if (!user.getEmail().equals(email)) {
                validEmail = false;
            }
        }
        if (validEmail) {
            if (!request.getEmail().isEmpty()) {
                user.setEmail(request.getEmail());
            }
        } else {
            return new ShowAlert(Alerts.emailV(), AlertType.Error);
        }
        if (!request.getName().isEmpty()) {
            user.setName(request.getName());
        }
        if (!request.getLastname().isEmpty()) {
            user.setLastName(request.getLastname());
        }
        if (!request.getBiography().isEmpty()) {
            user.setBiography(request.getBiography());
        }
        if (Objects.nonNull(request.getBirthday())) {
            user.setBirthDay(new DateTime(request.getBirthday()));
        }
        user.setPublicEmail(request.isPublicEmail());
        user.setPublicNumber(request.isPublicNumber());
        user.setPublicBirthday(request.isPublicBirthday());
        MainSaver.get().User.save(user);
        return new RebuildEditResponse(user);
    }


}
