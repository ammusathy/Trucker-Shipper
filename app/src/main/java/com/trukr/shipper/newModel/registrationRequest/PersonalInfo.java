package com.trukr.shipper.newModel.registrationRequest;

/**
 * Created by nijamudhin on 5/10/2016.
 */
public class PersonalInfo {
    private String Message;
    private String StatusCode;
    private String UserType;
    private String UserId;

    public void setMessage(String message) {
        this.Message = message;
    }

    public void setStatusCode(String statusCode) {
        this.StatusCode = statusCode;
    }

    public void setUserType(String userType) {
        this.UserType = userType;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public void setAuthToken(String authToken) {
        this.AuthToken = authToken;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    private String AuthToken;
    private Personal personal;

    public PersonalInfo() {
        Message = "";
        StatusCode = "";
        UserType = "";
        UserId = "";
        AuthToken = "";
        personal = new Personal();
    }

    public String getMessage() {
        return Message;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public String getUserType() {
        return UserType;
    }

    public String getUserId() {
        return UserId;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public Personal getpersonal() {
        return personal;
    }

    public class Personal {
        private String Firstname;
        private String Lastname;
        private String Email;
        private String MobileNumber;
        private String Street;
        private String City;

        public String getFirstname() {
            return Firstname;
        }

        public void setFirstname(String firstname) {
            this.Firstname = firstname;
        }

        public String getLastname() {
            return Lastname;
        }

        public void setLastname(String lastname) {
            this.Lastname = lastname;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            this.Email = email;
        }

        public String getMobileNumber() {
            return MobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.MobileNumber = mobileNumber;
        }

        public String getStreet() {
            return Street;
        }

        public void setStreet(String street) {
            this.Street = street;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            this.City = city;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            this.State = state;
        }

        public String getZipcode() {
            return Zipcode;
        }

        public void setZipcode(String zipcode) {
            this.Zipcode = zipcode;
        }

        public String getCompany() {
            return Company;
        }

        public void setCompany(String company) {
            this.Company = company;
        }

        public String getOfficeNumber() {
            return OfficeNumber;
        }

        public void setOfficeNumber(String officeNumber) {
            this.OfficeNumber = officeNumber;
        }

        public String getTaxId() {
            return TaxId;
        }

        public void setTaxId(String taxId) {
            this.TaxId = taxId;
        }

        private String State;
        private String Zipcode;
        private String Company;
        private String OfficeNumber;
        private String TaxId;

        public Personal() {
            Firstname = "";
            Lastname = "";
            Email = "";
            MobileNumber = "";
            Street = "";
            City = "";
            State = "";
            Zipcode = "";
            Company = "";
            OfficeNumber = "";
            TaxId = "";
        }

    }
}
