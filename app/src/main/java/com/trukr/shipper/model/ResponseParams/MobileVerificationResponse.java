package com.trukr.shipper.model.ResponseParams;

/**
 * Created by kalaivani on 3/15/2016.
 */
public class MobileVerificationResponse {
    private String StatusCode;
    private String Message;
    private String Userid;

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getAuthtoken() {
        return Authtoken;
    }

    public void setAuthtoken(String authtoken) {
        Authtoken = authtoken;
    }

    private String Authtoken;

    public MobileVerificationResponse() {
        StatusCode = "";
        Message = "";

    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


}
