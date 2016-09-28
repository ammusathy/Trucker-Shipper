package com.trukr.shipper.model.RequestParams;

public class MobileVerificationRequestParams {

    private String UserId;
    private String Mobile;
    private String ActivationCode;

    public MobileVerificationRequestParams() {
        UserId = "";
        Mobile = "";
        ActivationCode = "";
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

    public String getActivationCode() {
        return ActivationCode;
    }

    public void setActivationCode(String ActivationCode) {
        this.ActivationCode = ActivationCode;
    }
}

