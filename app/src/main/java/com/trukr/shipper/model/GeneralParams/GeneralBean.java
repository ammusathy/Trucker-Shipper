package com.trukr.shipper.model.GeneralParams;

public class GeneralBean {

    String Id, Value;

    public GeneralBean(){
        Id = "";
        Value = "";
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
