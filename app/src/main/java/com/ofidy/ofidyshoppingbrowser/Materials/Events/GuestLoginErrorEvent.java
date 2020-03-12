package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class GuestLoginErrorEvent {

    public final Throwable error;
    public final boolean wasInitiatedByUser;

    public GuestLoginErrorEvent(Throwable error,
                                boolean wasInitiatedByUser) {
        this.error = error;
        this.wasInitiatedByUser = wasInitiatedByUser;
    }

}
