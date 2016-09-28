package com.trukr.shipper.model.GeneralParams;

public class RequestKeyParams {
    String requestKey, requestValue;

    public RequestKeyParams() {
        requestKey = "";
        requestValue = "";
    }

    RequestKeyParams(String key, String value) {
        this.requestKey = key;
        this.requestValue = value;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getRequestValue() {
        return requestValue;
    }

    public void setRequestValue(String requestValue) {
        this.requestValue = requestValue;
    }
}
