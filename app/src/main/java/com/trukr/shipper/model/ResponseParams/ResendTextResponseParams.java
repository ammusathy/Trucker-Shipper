package com.trukr.shipper.model.ResponseParams;

/**
 * Created by kalaivani on 3/15/2016.
 */
public class ResendTextResponseParams {
    private String Statuscode;
    private String Message;
    private String mobilenumber;

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public ResendTextResponseParams() {
        Statuscode = "";
        Message = "";
        mobilenumber="";

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
