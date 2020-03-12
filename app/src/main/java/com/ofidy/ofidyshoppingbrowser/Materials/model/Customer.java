package com.ofidy.ofidyshoppingbrowser.Materials.model;


import java.util.ArrayList;

/**
 * Created by ari on 10/11/16.
 */
public class Customer {

    private String id;
    private String sid;
    private String firstName;
    private String lastName;
    private String email;
    private String currency;
    private String username;
    private String password;
    private String gender;
    private String mobile;
    private String telephone;
    private String dob;
    private String memWord;

    private ArrayList<Address> addresses;
    //accountType: "User",
    //transactions: [ ],

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String prefCurrency) {
        this.currency = prefCurrency;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(Address address) {
        if(addresses ==  null)
            addresses = new ArrayList<>();
        addresses.add(address);
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
