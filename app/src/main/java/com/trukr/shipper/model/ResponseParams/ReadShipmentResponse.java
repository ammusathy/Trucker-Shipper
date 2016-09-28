package com.trukr.shipper.model.ResponseParams;

/**
 * Created by kalaivani on 5/7/2016.
 */
public class ReadShipmentResponse {

    private String DeliveryDate;
    private String OrderStatus;
    private String Message;
    private String OrderId;
    private String UserId;
    private String PickupDate;
    private String AuthToken;
    private String OrderStatusLabel;
    private String PickupTime;
    private String DeliveryTime;
    private String TruckType;
    private String Notes;
    private int StatusCode, UserType;
    private PaymentResponse payment;
    private ShipmentResponse shipmentResponse;
    private DriverResponse driverResponse;
    private AccessoriesResponse accessoriesResponse;
    private ShipperResponse shipperResponse;

    public ReadShipmentResponse(String deliveryDate, String orderStatus, String message, String orderId, String userId, String pickupDate, String authToken, String orderStatusLabel, String pickupTime, String deliveryTime, String truckType, int statusCode, int userType, PaymentResponse payment, ShipmentResponse shipmentResponse, DriverResponse driverResponse, AccessoriesResponse accessoriesResponse, ShipperResponse shipperResponse) {
        DeliveryDate = deliveryDate;
        OrderStatus = orderStatus;
        Message = message;
        OrderId = orderId;
        UserId = userId;
        PickupDate = pickupDate;
        AuthToken = authToken;
        OrderStatusLabel = orderStatusLabel;
        PickupTime = pickupTime;
        DeliveryTime = deliveryTime;
        TruckType = truckType;
        StatusCode = statusCode;
        UserType = userType;
        this.payment = payment;
        this.shipmentResponse = shipmentResponse;
        this.driverResponse = driverResponse;
        this.accessoriesResponse = accessoriesResponse;
        this.shipperResponse = shipperResponse;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        this.Notes = notes;
    }


    public ReadShipmentResponse() {
    }

    public ReadShipmentResponse(String notes) {
        this.Notes = notes;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPickupDate() {
        return PickupDate;
    }

    public void setPickupDate(String pickupDate) {
        PickupDate = pickupDate;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public String getOrderStatusLabel() {
        return OrderStatusLabel;
    }

    public void setOrderStatusLabel(String orderStatusLabel) {
        OrderStatusLabel = orderStatusLabel;
    }

    public String getPickupTime() {
        return PickupTime;
    }

    public void setPickupTime(String pickupTime) {
        PickupTime = pickupTime;
    }

    public String getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    public String getTruckType() {
        return TruckType;
    }

    public void setTruckType(String truckType) {
        TruckType = truckType;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    public PaymentResponse getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }

    public ShipmentResponse getShipmentResponse() {
        return shipmentResponse;
    }

    public void setShipmentResponse(ShipmentResponse shipmentResponse) {
        this.shipmentResponse = shipmentResponse;
    }

    public DriverResponse getDriverResponse() {
        return driverResponse;
    }

    public void setDriverResponse(DriverResponse driverResponse) {
        this.driverResponse = driverResponse;
    }

    public AccessoriesResponse getAccessoriesResponse() {
        return accessoriesResponse;
    }

    public void setAccessoriesResponse(AccessoriesResponse accessoriesResponse) {
        this.accessoriesResponse = accessoriesResponse;
    }

    public ShipperResponse getShipperResponse() {
        return shipperResponse;
    }

    public void setShipperResponse(ShipperResponse shipperResponse) {
        this.shipperResponse = shipperResponse;
    }

}

