package com.ofidy.ofidyshoppingbrowser.Materials.model;

import android.text.TextUtils;

/**
 * Created by ari on 10/11/16.
 */
public class Cart {

    private String id;
    private String currency;
    private double unitPrice;
    private double totalPrice;
    private double adminShipping;
    private String name;
    private String weight;
    private String colour;
    private String isize;

    private long timestamp;
    private double srcregionshippingcost;
    private double srcregiontax;
    //private String link;
    private boolean size;
    private int quantity;
    private String promoCode;
    private int status;
    private String prodId;
    //private int blacklist;

    public Cart(String prodId, String name, String weight, String currency, int quantity, double price){
        this.prodId = prodId;
        this.name = name;
        this.unitPrice = price;
        this.weight = weight;
        this.currency = currency;
        this.quantity = quantity;
    }

    public String getId() {
        if(TextUtils.isEmpty(id))
            return prodId;
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        if(totalPrice == 0)
            return unitPrice * quantity;
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    public double getSrcregionshippingcost() {
        return srcregionshippingcost;
    }

    public void setSrcregionshippingcost(double srcregionshippingcost) {
        this.srcregionshippingcost = srcregionshippingcost;
    }

    public double getSrcregiontax() {
        return srcregiontax;
    }

    public void setSrcregiontax(double srcregiontax) {
        this.srcregiontax = srcregiontax;
    }

    public double getAdminShipping() {
        return adminShipping;
    }

    public void setAdminShipping(double adminShipping) {
        this.adminShipping = adminShipping;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getIsize() {
        return isize;
    }

    public void setIsize(String size) {
        this.isize = isize;
    }

}
