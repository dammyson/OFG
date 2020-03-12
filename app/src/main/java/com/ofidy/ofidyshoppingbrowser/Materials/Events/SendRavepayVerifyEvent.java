package com.ofidy.ofidyshoppingbrowser.Materials.Events;

/**
 * Created by Okaja on 15/02/2018.
 */

public class SendRavepayVerifyEvent {
    public final String ref;
    public final double amount;
    public SendRavepayVerifyEvent(String ref, double amount){
        this.ref= ref;
        this. amount= amount;
    }
}

