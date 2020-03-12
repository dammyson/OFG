package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LoginDoneEvent {

    public final String id;
    public final String currency;
    public final String body;
    public final String email;
    public final String password;
    public final String sid;
    public final boolean wasInitiatedByUser;

    public LoginDoneEvent(String id, String sid, String currency, String email, String password,
                          String body, boolean wasInitiatedByUser) {
        this.body = body;
        this.currency = currency;
        this.id = id;
        this.password = password;
        this.email = email;
        this.sid = sid;
        this.wasInitiatedByUser = wasInitiatedByUser;
    }

}
