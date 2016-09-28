package com.trukr.shipper.model.ResponseParams;

/**
 * Created by kalaivani on 5/7/2016.
 */
public class SourceResponse {

  String  FromLatitude, FromAddress, FromLongitude;

    public SourceResponse() {
    }

    public SourceResponse(String fromLatitude, String fromAddress, String fromLongitude) {

        FromLatitude = fromLatitude;
        FromAddress = fromAddress;
        FromLongitude = fromLongitude;
    }

    public String getFromLatitude() {

        return FromLatitude;
    }

    public void setFromLatitude(String fromLatitude) {
        FromLatitude = fromLatitude;
    }

    public String getFromAddress() {
        return FromAddress;
    }

    public void setFromAddress(String fromAddress) {
        FromAddress = fromAddress;
    }

    public String getFromLongitude() {
        return FromLongitude;
    }

    public void setFromLongitude(String fromLongitude) {
        FromLongitude = fromLongitude;
    }
}
