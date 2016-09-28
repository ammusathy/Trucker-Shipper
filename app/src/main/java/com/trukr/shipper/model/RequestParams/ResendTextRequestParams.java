package com.trukr.shipper.model.RequestParams;

public class ResendTextRequestParams {

    private String UserId;
    private String Mobile;

    public ResendTextRequestParams() {
        UserId = "";
        Mobile = "";
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }
}
