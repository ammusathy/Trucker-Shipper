package com.trukr.shipper.model.ResponseParams;

public class ProfileDetailsResponseParams {

    private String Message;
    private String StatusCode;
    private String UserType;
    private String UserId;
    private String AuthToken;
    private Personal Personal;

    public ProfileDetailsResponseParams() {
        Message = "";
        StatusCode = "";
        UserType = "";
        UserId = "";
        AuthToken = "";
        Personal = new Personal();
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String AuthToken) {
        this.AuthToken = AuthToken;
    }

    public Personal getPersonal() {
        return Personal;
    }

    public void setPersonal(Personal Personal) {
        this.Personal = Personal;
    }

    public class Personal {
        private String Firstname;
        private String Lastname;
        private String Email;
        private String MobileNumber;
        private String Street;
        private String City;
        private String State;
        private String Zipcode;
        private String Company;
        private String OfficeNumber;
        private String TaxId;
        private String ProfilePicture;

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
            ProfilePicture = "";
        }

        public String getMobileNumber() {
            return MobileNumber;
        }

        public void setMobileNumber(String MobileNumber) {
            this.MobileNumber = MobileNumber;
        }

        public String getFirstname() {
            return Firstname;
        }

        public void setFirstname(String Firstname) {
            this.Firstname = Firstname;
        }

        public String getLastname() {
            return Lastname;
        }

        public void setLastname(String Lastname) {
            this.Lastname = Lastname;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getStreet() {
            return Street;
        }

        public void setStreet(String Street) {
            this.Street = Street;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
        }

        public String getZipcode() {
            return Zipcode;
        }

        public void setZipcode(String Zipcode) {
            this.Zipcode = Zipcode;
        }

        public String getCompany() {
            return Company;
        }

        public void setCompany(String Company) {
            this.Company = Company;
        }

        public String getOfficeNumber() {
            return OfficeNumber;
        }

        public void setOfficeNumber(String OfficeNumber) {
            this.OfficeNumber = OfficeNumber;
        }

        public String getTaxId() {
            return TaxId;
        }

        public void setTaxId(String TaxId) {
            this.TaxId = TaxId;
        }

        public String getProfilePicture() {
            return ProfilePicture;
        }

        public void setProfilePicture(String ProfilePicture) {
            this.ProfilePicture = ProfilePicture;
        }
    }
}


