package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class SendSimplePayTokenErrorEvent {

    public final String message;

    public SendSimplePayTokenErrorEvent(String dest) {
        this.message = dest;
    }

}
