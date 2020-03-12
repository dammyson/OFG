package com.ofidy.ofidyshoppingbrowser.Materials.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by Ari on 10/11/2016.
 */

public class Address implements Parcelable {

    private String id;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressDescription;
    private String city;
    private String state;
    private String country;
    private String countryName;
    private int addressType;
    private String postcode;
    private String isDeliveryAddress;
    private String isCorrespondenceAddress;

    public Address() {
    }

    public Address(Parcel in) {
        id = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        addressLine3 = in.readString();
        addressDescription = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        countryName = in.readString();
        addressType = in.readInt();
        postcode = in.readString();
        isCorrespondenceAddress = in.readString();
        isDeliveryAddress = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine1(String ad) {
        addressLine1 = ad;
    }

    public void setAddressLine2(String ad) {
        addressLine2 = ad;
    }

    public void setAddressLine3(String ad) {
        addressLine3 = ad;
    }

    public String getAddressDescription() {
        return addressDescription;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String c) {
        city = c;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String c) {
        country = c;
    }

    public int getAddressType() {
        return addressType;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String p) {
        postcode = p;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(addressLine1);
        parcel.writeString(addressLine2);
        parcel.writeString(addressLine3);
        parcel.writeString(addressDescription);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(country);
        parcel.writeString(countryName);
        parcel.writeInt(addressType);
        parcel.writeString(postcode);
        parcel.writeString(isCorrespondenceAddress);
        parcel.writeString(isDeliveryAddress);
    }

    public String getIsDeliveryAddress() {
        return isDeliveryAddress;
    }

    public String getIsCorrespondenceAddress() {
        return isCorrespondenceAddress;
    }

    public String getFullAddress(){
        StringBuilder sb = new StringBuilder();
        sb.append(addressLine1);
        if(!TextUtils.isEmpty(addressLine2)){
            sb.append(", ").append(addressLine2);
            if(!TextUtils.isEmpty(addressLine3)){
                sb.append(", ").append(addressLine3);
            }
        }
        if(!TextUtils.isEmpty(city))
            sb.append(", ").append(city);
        sb.append(", ").append(state).append(", ").append(country);
        return sb.toString();
    }
}
