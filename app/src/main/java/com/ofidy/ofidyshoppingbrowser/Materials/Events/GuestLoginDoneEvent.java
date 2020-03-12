package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class GuestLoginDoneEvent {

    public final String id;
    public final String currency;
    public final String sid;
    public final boolean wasInitiatedByUser;

    public GuestLoginDoneEvent(String id, String sid, String currency, boolean wasInitiatedByUser) {
        this.currency = currency;
        this.id = id;
        this.sid = sid;
        this.wasInitiatedByUser = wasInitiatedByUser;
    }

}
