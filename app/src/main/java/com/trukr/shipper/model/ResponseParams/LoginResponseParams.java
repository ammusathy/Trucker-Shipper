package com.trukr.shipper.model.ResponseParams;

import java.util.ArrayList;

public class LoginResponseParams {

    private String Message;
    private String isActivated;
    private String isPasswordChanged;
    private String StatusCode;
    private String UserType;
    private String UserId;
    private String AuthToken;
    private String OrderStatus;
    private Login Login;
    private Payment Payment;

    LoginResponseParams() {
        Message = "";
        isActivated = "";
        isPasswordChanged = "";
        StatusCode = "";
        UserType = "";
        UserId = "";
        AuthToken = "";
        OrderStatus = "";
        Login = new Login();
        Payment = new Payment();
    }

    // get the response message
    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(String isActivated) {
        this.isActivated = isActivated;
    }

    public String getIsPasswordChanged() {
        return isPasswordChanged;
    }

    public void setIsPasswordChanged(String isPasswordChanged) {
        this.isPasswordChanged = isPasswordChanged;
    }

    // get the response messsage
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

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public Login getLogin() {
        return Login;
    }

    public void setLogin(Login Login) {
        this.Login = Login;
    }

    public Payment getPayment() {
        return Payment;
    }

    public void setPayment(Payment Payment) {
        this.Payment = Payment;
    }

    public class Payment {

        private ArrayList<Card> Cards = new ArrayList<Card>();

        public Payment() {
            Cards = new ArrayList<Card>();
        }

        public ArrayList<Card> getCards() {
            return Cards;
        }

        public void setCards(ArrayList<Card> Cards) {
            this.Cards = Cards;
        }
    }

    public class Login {
        private String Firstname;
        private String Lastname;
        private String Email;
        private String MobileNumber;
        private String Address;
        private String Street;
        private String City;
        private String State;
        private String Company;
        private String OfficeNumber;
        private String TaxId;

        public String getZipCode() {
            return ZipCode;
        }

        public void setZipCode(String zipCode) {
            ZipCode = zipCode;
        }

        private String ZipCode;
        private String ProfilePicture;

        public String getStreet() {
            return Street;
        }

        public void setStreet(String street) {
            Street = street;
        }


        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }


        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }


        public String getFirstname() {
            return Firstname;
        }

        public Login() {
            Firstname = "";
            Lastname = "";
            Email = "";
            MobileNumber = "";
            Address = "";
            Street = "";
            City = "";
            State = "";
            Company = "";
            OfficeNumber = "";
            TaxId = "";
            ProfilePicture = "";
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

        public String getMobileNumber() {
            return MobileNumber;
        }

        public void setMobileNumber(String MobileNumber) {
            this.MobileNumber = MobileNumber;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
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

    public class Card {

        private String MyCardId;
        private String PaymentMode;
        private String CardTypeId;
        private String CardType;
        private String CardNumber;
        private String NameOnCard;
        private String CardName;
        private String DefaultPaymentCard;
        private String MethodId;
        private ExpiryOn ExpiryOn;

        public Card() {
            MyCardId = "";
            PaymentMode = "";
            CardTypeId = "";
            CardType = "";
            CardNumber = "";
            NameOnCard = "";
            CardName = "";
            DefaultPaymentCard = "";
            MethodId = "";
            ExpiryOn = new ExpiryOn();
        }

        public String getMyCardId() {
            return MyCardId;
        }

        public void setMyCardId(String MyCardId) {
            this.MyCardId = MyCardId;
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

        public String getCardType() {
            return CardType;
        }

        public void setCardType(String CardType) {
            this.CardType = CardType;
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

        public String getCardName() {
            return CardName;
        }

        public void setCardName(String CardName) {
            this.CardName = CardName;
        }

        public String getDefaultPaymentCard() {
            return DefaultPaymentCard;
        }

        public void setDefaultPaymentCard(String DefaultPaymentCard) {
            this.DefaultPaymentCard = DefaultPaymentCard;
        }

        public String getMethodId() {
            return MethodId;
        }

        public void setMethodId(String MethodId) {
            this.MethodId = MethodId;
        }

        public ExpiryOn getExpiryOn() {
            return ExpiryOn;
        }

        public void setExpiryOn(ExpiryOn ExpiryOn) {
            this.ExpiryOn = ExpiryOn;
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