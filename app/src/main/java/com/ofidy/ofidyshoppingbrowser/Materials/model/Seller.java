package com.ofidy.ofidyshoppingbrowser.Materials.model;

/**
 * Created by Ari on 09/11/2016.
 */

public class Seller {

    private String agentID;
    private String currency;
    private double rprice;
    private String wholesale1;
    private String wholedescr1;
    private String wholesale2;
    private String wholedescr2;
    private String wholesale3;
    private String wholedescr3;
    private String wholesale4;
    private String wholedescr4;
    private String wholesale5;
    private String wholedescr5;

    public double getRprice() {
        return rprice;
    }

    public void setRprice(double rprice) {
        this.rprice = rprice;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
