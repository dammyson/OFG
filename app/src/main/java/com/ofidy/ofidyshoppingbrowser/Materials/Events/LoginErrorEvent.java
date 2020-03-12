package com.ofidy.ofidyshoppingbrowser.Materials.Events;

import android.support.annotation.Nullable;

public class LoginErrorEvent {

    public final Throwable error;
    public final String email;
    public final boolean wasInitiatedByUser;

    public LoginErrorEvent(Throwable error, @Nullable String email,
                           boolean wasInitiatedByUser) {
        this.error = error;
        this.email = email;
        this.wasInitiatedByUser = wasInitiatedByUser;
    }

}
