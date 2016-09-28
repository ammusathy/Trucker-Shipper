package com.trukr.shipper.model.ResponseParams;

/**
 * Created by nijamudhin on 5/24/2016.
 */
public class ShipperResponse {

    private String ShipperProfile, ShipperContact, ShipperName, Street, State, FirstName, LastName, Zipcode, City;

    public ShipperResponse() {
    }

    public ShipperResponse(String shipperProfile, String shipperContact, String shipperName, String street, String state, String firstName, String lastName, String zipcode, String city) {

        ShipperProfile = shipperProfile;
        ShipperContact = shipperContact;
        ShipperName = shipperName;
        Street = street;
        State = state;
        FirstName = firstName;
        LastName = lastName;
        Zipcode = zipcode;
        City = city;
    }

    public String getShipperProfile() {

        return ShipperProfile;
    }

    public void setShipperProfile(String shipperProfile) {
        ShipperProfile = shipperProfile;
    }

    public String getShipperContact() {
        return ShipperContact;
    }

    public void setShipperContact(String shipperContact) {
        ShipperContact = shipperContact;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}

