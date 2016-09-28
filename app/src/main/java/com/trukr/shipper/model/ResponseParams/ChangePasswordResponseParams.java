package com.trukr.shipper.model.ResponseParams;

/**
 * Created by nijamudhin on 5/5/2016.
 */
public class ChangePasswordResponseParams {
    private String StatusCode;
    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public ChangePasswordResponseParams() {
        StatusCode = "";
        Message = "";
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }


}
