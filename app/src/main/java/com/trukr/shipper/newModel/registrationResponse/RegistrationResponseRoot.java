package com.trukr.shipper.newModel.registrationResponse;


import com.trukr.shipper.newModel.CommonResponse;

/**
 * Created by kalaivani on 3/24/2016.
 */
public class RegistrationResponseRoot extends CommonResponse {

    public User user;

    public RegistrationResponseRoot() {
    }

    public RegistrationResponseRoot(User user) {

        this.user = user;
    }

    public RegistrationResponseRoot(String statusCode, String message, User user) {
        super(statusCode, message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
