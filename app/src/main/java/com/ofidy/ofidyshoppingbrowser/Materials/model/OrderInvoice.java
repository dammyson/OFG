package com.ofidy.ofidyshoppingbrowser.Materials.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by Ari on 28/11/2016.
 */

public class OrderInvoice implements Parcelable {

    private double productsBill;
    private String invoiceid;
    private String addr1;
    private String addr2;
    private String addr3;
    private String city;
    private String state;
    private String country;
    private String country_sp;
    private String postcode;
    private double shipBill;
    private String currency;
    private String payMethod;
    private double fullBill;
    private double othercost;

    public OrderInvoice(Parcel in) {
        productsBill = in.readDouble();
        invoiceid = in.readString();
        addr1 = in.readString();
        addr2 = in.readString();
        addr3 = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        country_sp = in.readString();
        postcode = in.readString();
        shipBill = in.readDouble();
        currency = in.readString();
        payMethod = in.readString();
        fullBill = in.readDouble();
        othercost = in.readDouble();
    }

    public static final Creator<OrderInvoice> CREATOR = new Creator<OrderInvoice>() {
        @Override
        public OrderInvoice createFromParcel(Parcel in) {
            return new OrderInvoice(in);
        }

        @Override
        public OrderInvoice[] newArray(int size) {
            return new OrderInvoice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(productsBill);
        parcel.writeString(invoiceid);
        parcel.writeString(addr1);
        parcel.writeString(addr2);
        parcel.writeString(addr3);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(country);
        parcel.writeString(country_sp);
        parcel.writeString(postcode);
        parcel.writeDouble(shipBill);
        parcel.writeString(currency);
        parcel.writeString(payMethod);
        parcel.writeDouble(fullBill);
        parcel.writeDouble(othercost);
    }

    public String getInvoiceId(){
        return invoiceid;
    }

    public Double getFullBill(){
        //if(fullBill > 0)
            return fullBill;
        //return productsBill + shipBill;
    }

    public Double getShipBill(){
        return shipBill;
    }

    public Double getProductsBill(){
        return productsBill;
    }

    public Double getOtherCost(){
        return othercost;
    }

    public String getCurrency(){
        return currency;
    }

    public String getState(){
        return state;
    }

    public String getCountrySp(){
        return country_sp;
    }

    public String getPostcode(){
        return postcode;
    }

    public String getFullAddress(){
        StringBuilder sb = new StringBuilder();
        sb.append(addr1);
        if(!TextUtils.isEmpty(addr2)){
            sb.append(", ").append(addr2);
            if(!TextUtils.isEmpty(addr3)){
                sb.append(", ").append(addr3);
            }
        }
        if(!TextUtils.isEmpty(city))
            sb.append(", ").append(city);
        sb.append(", ").append(state).append(", ").append(country);
        return sb.toString();
    }

    public String getPayMethod() {
        return payMethod;
    }
}
