package com.trukr.shipper.newModel.registrationRequest;

/**
 * Created by kalaivani on 3/25/2016.
 */
public class ExpiryOn {

    private String MM;
    private String YY;

    public ExpiryOn(String MM, String YY) {
        this.MM = MM;
        this.YY = YY;
    }

    public ExpiryOn() {
    }

    public String getMM() {

        return MM;
    }

    public void setMM(String MM) {
        this.MM = MM;
    }

    public String getYY() {
        return YY;
    }

    public void setYY(String YY) {
        this.YY = YY;
    }
}

