package com.trukr.shipper.model.RequestParams;

public class RegistrationBean {

    private String UserType;
    private Personal Personal;
    private Payment Payment;

    public RegistrationBean() {
        UserType = "";
        Personal = new Personal();
        Payment = new Payment();
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public Personal getPersonal() {
        return Personal;
    }

    public void setPersonal(Personal Personal) {
        this.Personal = Personal;
    }

    public Payment getPayment() {
        return Payment;
    }

    public void setPayment(Payment Payment) {
        this.Payment = Payment;
    }

    /**
     * Personal Details
     */
    public class Personal {
        private String FirstName;
        private String LastName;
        private String Email;
        private String Password;
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
            Password = "";
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

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
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

    /**
     * PaymentInfo Details
     */
    public class Payment {

        private String PaymentMode;
        private String CardTypeId;
        private String CardNumber;
        private String NameOnCard;
        private ExpiryOn ExpiryOn;
        private String CardName;

        public Payment() {
            PaymentMode = "";
            CardTypeId = "";
            CardNumber = "";
            CardName = "";
            NameOnCard = "";
            ExpiryOn = new ExpiryOn();
        }

        public String getPaymentMode() {
            return PaymentMode;
        }

        public void setPaymentMode(String PaymentMode) {
            this.PaymentMode = PaymentMode;
        }

        public String getCardTypeId() {
            return CardTypeId;
        }

        public void setCardTypeId(String CardTypeId) {
            this.CardTypeId = CardTypeId;
        }

        public String getCardNumber() {
            return CardNumber;
        }

        public void setCardNumber(String CardNumber) {
            this.CardNumber = CardNumber;
        }

        public String getNameOnCard() {
            return NameOnCard;
        }

        public void setNameOnCard(String NameOnCard) {
            this.NameOnCard = NameOnCard;
        }

        public ExpiryOn getExpiryOn() {
            return ExpiryOn;
        }

        public void setExpiryOn(ExpiryOn ExpiryOn) {
            this.ExpiryOn = ExpiryOn;
        }

        public String getCardName() {
            return CardName;
        }

        public void setCardName(String CardName) {
            this.CardName = CardName;
        }

        public class ExpiryOn {

            private String MM;
            private String YY;

            public ExpiryOn() {
                MM = "";
                YY = "";
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
    }
}