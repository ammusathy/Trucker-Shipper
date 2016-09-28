package com.trukr.shipper.model.GeneralParams;

public class GeneralRequestParams {

    private String AuthToken;
    private String UserId;

    public GeneralRequestParams() {
        AuthToken = "";
        UserId = "";
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String AuthToken) {
        this.AuthToken = AuthToken;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }
}
