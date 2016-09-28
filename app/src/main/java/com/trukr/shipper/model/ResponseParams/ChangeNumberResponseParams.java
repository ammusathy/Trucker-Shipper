package com.trukr.shipper.model.ResponseParams;

/**
 * Created by kalaivani on 3/15/2016.
 */
public class ChangeNumberResponseParams {

    private String Statuscode;
    private String Message;
    private String Authtoken;
    private String Mobilenumber;

    public String getAuthtoken() {
        return Authtoken;
    }

    public void setAuthtoken(String authtoken) {
        Authtoken = authtoken;
    }

    public String getMobilenumber() {
        return Mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        Mobilenumber = mobilenumber;
    }

    public ChangeNumberResponseParams() {
        Statuscode = "";
        Message = "";
        Authtoken="";
        Mobilenumber="";



    }

    public String getStatuscode() {
        return Statuscode;
    }

    public void setStatuscode(String statuscode) {
        Statuscode = statuscode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
