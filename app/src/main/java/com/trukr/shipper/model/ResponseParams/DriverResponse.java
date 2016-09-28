package com.trukr.shipper.model.ResponseParams;

/**
 * Created by kalaivani on 5/7/2016.
 */
public class DriverResponse {

    private String USDotNumber, DriverContact, DriverName, DriverLicense, DriverImage, Insurance, LicensePlates, TruckSafetyRating,
            MCNumber;

    public DriverResponse() {
    }

    public DriverResponse(String USDotNumber, String driverContact, String driverName, String driverLicense, String driverImage, String insurance, String licensePlates, String truckSafetyRating, String MCNumber) {

        this.USDotNumber = USDotNumber;
        DriverContact = driverContact;
        DriverName = driverName;
        DriverLicense = driverLicense;
        DriverImage = driverImage;
        Insurance = insurance;
        LicensePlates = licensePlates;
        TruckSafetyRating = truckSafetyRating;
        this.MCNumber = MCNumber;
    }

    public String getUSDotNumber() {

        return USDotNumber;
    }

    public void setUSDotNumber(String USDotNumber) {
        this.USDotNumber = USDotNumber;
    }

    public String getDriverContact() {
        return DriverContact;
    }

    public void setDriverContact(String driverContact) {
        DriverContact = driverContact;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getDriverLicense() {
        return DriverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        DriverLicense = driverLicense;
    }

    public String getDriverImage() {
        return DriverImage;
    }

    public void setDriverImage(String driverImage) {
        DriverImage = driverImage;
    }

    public String getInsurance() {
        return Insurance;
    }

    public void setInsurance(String insurance) {
        Insurance = insurance;
    }

    public String getLicensePlates() {
        return LicensePlates;
    }

    public void setLicensePlates(String licensePlates) {
        LicensePlates = licensePlates;
    }

    public String getTruckSafetyRating() {
        return TruckSafetyRating;
    }

    public void setTruckSafetyRating(String truckSafetyRating) {
        TruckSafetyRating = truckSafetyRating;
    }

    public String getMCNumber() {
        return MCNumber;
    }

    public void setMCNumber(String MCNumber) {
        this.MCNumber = MCNumber;
    }
}
