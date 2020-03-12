package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoadPaymentInfoErrorEvent {

    public final String message;

    public LoadPaymentInfoErrorEvent(String dest) {
        this.message = dest;
    }

}
