package com.trukr.shipper.newModel;

/**
 * Created by kalaivani on 3/24/2016.
 */
public class CommonResponse {

    public String StatusCode, Message;

    public CommonResponse() {
    }

    public CommonResponse(String statusCode, String message) {

        StatusCode = statusCode;
        Message = message;
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
