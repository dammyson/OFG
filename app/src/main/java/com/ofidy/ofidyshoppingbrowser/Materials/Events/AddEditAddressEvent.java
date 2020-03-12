package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class AddEditAddressEvent {

    public final String addrLine1;
    public final String city;
    public final String country;
    public final String state;
    public final String addrLine2;
    public final String area;
    public final String desc;
    public final String postcode;
    public final int addressType;
    public final String cor;
    public final String del;
    public final boolean isPrimary;
    public final boolean edit;

    public AddEditAddressEvent(String addrLine1, String addrLine2, String area, String city, String state,
                               String country, String desc, String postcode, int addressType, String cor,
                               String del, boolean isPrimary, boolean edit) {
        this.addrLine1 = addrLine1;
        this.city = city;
        this.country = country;
        this.state = state;
        this.addrLine2 = addrLine2;
        this.area = area;
        this.desc = desc;
        this.postcode = postcode;
        this.addressType = addressType;
        this.cor = cor;
        this.del = del;
        this.isPrimary = isPrimary;
        this.edit = edit;
    }

}
