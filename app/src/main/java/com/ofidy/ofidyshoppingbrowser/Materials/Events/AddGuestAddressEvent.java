package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class AddGuestAddressEvent {

    public final String addrLine1;
    public final String city;
    public final String country;
    public final String state;
    public final String addrLine2;
    public final String area;
    public final String desc;
    public final String postcode;
    public final int addressType;
    public final boolean isPrimary;
    public final String email;
    public final String fname;
    public final String lname;
    public final String phone;

    public AddGuestAddressEvent(String addrLine1, String addrLine2, String area, String city, String state,
                                String country, String desc, String postcode, int addressType,
                                boolean isPrimary, String email, String fname, String lname, String phone) {
        this.addrLine1 = addrLine1;
        this.city = city;
        this.country = country;
        this.state = state;
        this.addrLine2 = addrLine2;
        this.area = area;
        this.desc = desc;
        this.postcode = postcode;
        this.addressType = addressType;
        this.isPrimary = isPrimary;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
    }

}
