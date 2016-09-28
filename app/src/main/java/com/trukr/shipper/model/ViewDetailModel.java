package com.trukr.shipper.model;

/**
 * Created by nijamudhin on 6/17/2016.
 */
public class ViewDetailModel {

    public String id, serviceName, amount, count,value;

    public ViewDetailModel() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ViewDetailModel(String id, String serviceName, String amount, String count, String value) {

        this.id = id;
        this.serviceName = serviceName;
        this.amount = amount;
        this.count = count;
        this.value = value;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}