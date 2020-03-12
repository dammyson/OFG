package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class SendSimplePayTokenEvent {

    public final String token;
    public final String amount;

    public SendSimplePayTokenEvent(String token, String amount) {
        this.token = token;
        this.amount = amount;
    }

}
