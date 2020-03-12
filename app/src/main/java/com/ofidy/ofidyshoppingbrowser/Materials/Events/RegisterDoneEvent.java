package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class RegisterDoneEvent {

    public final String id;
    public final String body;
    public final boolean wasInitiatedByUser;

    public RegisterDoneEvent(String id, String body, boolean wasInitiatedByUser) {
        this.body = body;
        this.id = id;
        this.wasInitiatedByUser = wasInitiatedByUser;
    }

}
