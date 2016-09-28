package com.trukr.shipper.model.ResponseParams;

/**
 * Created by nijamudhin on 5/19/2016.
 */

public class AllShipmentResponse {

    private String OrderId;
    private String OrderStatus;
    private String OrderDate;
    private String OrderTime;
    private String FromAddress;
    private String TotalAmount;


    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getFromAddress() {
        return FromAddress;
    }

    public void setFromAddress(String fromAddress) {
        FromAddress = fromAddress;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
}
