package com.ofidy.ofidyshoppingbrowser.Materials.Events;

/**
 * Created by Okaja on 28/03/2018.
 */

public class SendBankTransferCancelDoneEvent {
    public final String message;

    public SendBankTransferCancelDoneEvent(String dest) {
        this.message = dest;
    }
}
