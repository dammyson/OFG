package com.ofidy.ofidyshoppingbrowser.Materials.model;

/**
 * Created by Ari on 01/12/2016.
 */

public class Transaction {

    private String invoiceId;
    private String sid;
    private String startDate;
    private String status;
    private String currency;
    private String itemCount;
    private String delivered;

    private String transDate;

    private String prodPrice;
    private String totalPrice;

    private String quantity;

    public String getId() {
        return invoiceId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
}
