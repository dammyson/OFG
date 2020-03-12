package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class UpdateCartItemEvent {

    public final String tid;
    public final int quantity;
    public final String promoCode;

    public UpdateCartItemEvent(String tid, int quantity, String promoCode) {
        this.tid = tid;
        this.quantity = quantity;
        this.promoCode = promoCode;
    }

}
