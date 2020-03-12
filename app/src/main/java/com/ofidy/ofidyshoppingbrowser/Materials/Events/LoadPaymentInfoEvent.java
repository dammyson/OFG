package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadPaymentInfoEvent {

    public final String billAdd;
    public final String shipAdd;
    public final int shipMethod;
    public final String payMethod;
    public final String shipReq;

    public LoadPaymentInfoEvent(String billAdd, String shipAdd, int shipMethod, String payMethod, String shipReq) {
        this.billAdd = billAdd;
        this.shipAdd = shipAdd;
        this.shipMethod = shipMethod;
        this.payMethod = payMethod;
        this.shipReq = shipReq;
    }

}
