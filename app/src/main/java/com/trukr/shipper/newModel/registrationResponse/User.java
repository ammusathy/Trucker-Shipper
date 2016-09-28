package com.trukr.shipper.newModel.registrationResponse;

/**
 * Created by kalaivani on 3/24/2016.
 */
public class User {

    public String UserId, Mobile;

    public User() {
    }

    public User(String userId, String mobile) {

        UserId = userId;
        Mobile = mobile;
    }

    public String getUserId() {

        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}
