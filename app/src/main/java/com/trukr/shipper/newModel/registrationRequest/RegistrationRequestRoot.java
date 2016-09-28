package com.trukr.shipper.newModel.registrationRequest;

/**
 * Created by kalaivani on 3/24/2016.
 */
public class RegistrationRequestRoot {

    public String UserType;
    public Personal personal;

    public RegistrationRequestRoot(String userType, Personal personal) {
        UserType = userType;
        this.personal = personal;
    }

    public RegistrationRequestRoot() {

    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }
}
