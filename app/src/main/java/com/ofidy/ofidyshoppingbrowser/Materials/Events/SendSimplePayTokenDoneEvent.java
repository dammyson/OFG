package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class SendSimplePayTokenDoneEvent {

    public final String message;

    public SendSimplePayTokenDoneEvent(String dest) {
        this.message = dest;
    }

}
