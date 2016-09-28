package com.trukr.shipper.model.RequestParams;

public class UpdateProfileRequestParams {

    private String UserType;
    private String UserId;
    private String AuthToken;
    private Personal Personal;

    public UpdateProfileRequestParams() {
        UserType = "";
        UserId = "";
        AuthToken = "";
        Personal = new Personal();
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

        private String FirstName;
        private String LastName;
        private String Email;
        private String Mobile;
        private String Company;
        private String Street;
        private String City;
        private String State;
        private String Zipcode;
        private String TaxId;
        private String OfficeNumber;
        private String ProfilePicture;

        public Personal() {
            FirstName = "";
            LastName = "";
            Email = "";
            Mobile = "";
            Company = "";
            Street = "";
            City = "";
            State = "";
            Zipcode = "";
            TaxId = "";
            OfficeNumber = "";
            ProfilePicture = "";
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String FirstName) {
            this.FirstName = FirstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String LastName) {
            this.LastName = LastName;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public String getCompany() {
            return Company;
        }

        public void setCompany(String Company) {
            this.Company = Company;
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

        public String getTaxId() {
            return TaxId;
        }

        public void setTaxId(String TaxId) {
            this.TaxId = TaxId;
        }

        public String getOfficeNumber() {
            return OfficeNumber;
        }

        public void setOfficeNumber(String OfficeNumber) {
            this.OfficeNumber = OfficeNumber;
        }

        public String getProfilePicture() {
            return ProfilePicture;
        }

        public void setProfilePicture(String ProfilePicture) {
            this.ProfilePicture = ProfilePicture;
        }
    }

}
