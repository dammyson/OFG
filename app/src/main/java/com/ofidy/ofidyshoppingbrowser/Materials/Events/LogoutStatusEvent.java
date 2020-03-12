package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class LogoutStatusEvent {

    public final boolean succeeded;
    public final boolean hasPendingActions;

    public LogoutStatusEvent(boolean succeeded, boolean hasPendingActions) {
        this.succeeded = succeeded;
        this.hasPendingActions = hasPendingActions;
    }

}
