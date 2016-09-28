package com.trukr.shipper.model.RequestParams;

/**
 * Created by kalaivani on 3/15/2016.
 */
public class ChangeNumberRequestParams {
    private String UserId;
    private String Mobile;
    private String AuthToken;
    public ChangeNumberRequestParams() {
        UserId = "";
        Mobile = "";
        AuthToken="";

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

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }
}
