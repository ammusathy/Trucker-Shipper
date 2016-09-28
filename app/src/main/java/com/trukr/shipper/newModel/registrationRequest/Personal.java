package com.trukr.shipper.newModel.registrationRequest;

/**
 * Created by kalaivani on 3/24/2016.
 */
public class Personal {
    public String FirstName,
            LastName,
            Email,
            Password,
            Mobile,
            Company,
            Street,
            City,
            State,
            Zipcode,
            TaxId,
            BrokerId,
            OfficeNumber,
            ProfilePicture;

    public Personal() {
    }

    public Personal(String firstName, String lastName, String email, String password, String mobile, String company, String street, String city, String state, String zipcode, String taxId, String brokerId, String officeNumber, String profilePicture) {

        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Password = password;
        Mobile = mobile;
        Company = company;
        Street = street;
        City = city;
        State = state;
        Zipcode = zipcode;
        TaxId = taxId;
        BrokerId = brokerId;
        OfficeNumber = officeNumber;
        ProfilePicture = profilePicture;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

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

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }

    public String getTaxId() {
        return TaxId;
    }

    public void setTaxId(String taxId) {
        TaxId = taxId;
    }

    public String getBrokerId() {
        return BrokerId;
    }

    public void setBrokerId(String brokerId) {
        BrokerId = brokerId;
    }

    public String getOfficeNumber() {
        return OfficeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        OfficeNumber = officeNumber;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }
}
