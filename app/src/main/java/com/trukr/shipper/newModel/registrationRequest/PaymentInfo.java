package com.trukr.shipper.newModel.registrationRequest;

/**
 * Created by kalaivani on 3/25/2016.
 */
public class PaymentInfo {

    private String PaymentMode;
    private String CardTypeId;
    private String CardNumber;
    private String NameOnCard;
    private String CardName;
    public ExpiryOn expiryOn;

    public PaymentInfo() {
    }

    public PaymentInfo(String paymentMode, String cardTypeId, String cardNumber, String nameOnCard, String cardName, ExpiryOn expiryOn) {

        PaymentMode = paymentMode;
        CardTypeId = cardTypeId;
        CardNumber = cardNumber;
        NameOnCard = nameOnCard;
        CardName = cardName;
        this.expiryOn = expiryOn;
    }

    public String getPaymentMode() {

        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getCardTypeId() {
        return CardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        CardTypeId = cardTypeId;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getNameOnCard() {
        return NameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        NameOnCard = nameOnCard;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public ExpiryOn getExpiryOn() {
        return expiryOn;
    }

    public void setExpiryOn(ExpiryOn expiryOn) {
        this.expiryOn = expiryOn;
    }
}