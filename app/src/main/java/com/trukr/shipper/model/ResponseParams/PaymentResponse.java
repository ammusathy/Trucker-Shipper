package com.trukr.shipper.model.ResponseParams;

/**
 * Created by kalaivani on 5/7/2016.
 */
public class PaymentResponse {

    private String DeliveryCharges, Total, PaymentMethod, TotalPayable;

    public PaymentResponse() {
    }

    public PaymentResponse(String deliveryCharges, String total, String paymentMethod, String totalPayable) {

        DeliveryCharges = deliveryCharges;
        Total = total;
        PaymentMethod = paymentMethod;
        TotalPayable = totalPayable;
    }

    public String getDeliveryCharges() {

        return DeliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        DeliveryCharges = deliveryCharges;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getTotalPayable() {
        return TotalPayable;
    }

    public void setTotalPayable(String totalPayable) {
        TotalPayable = totalPayable;
    }
}
