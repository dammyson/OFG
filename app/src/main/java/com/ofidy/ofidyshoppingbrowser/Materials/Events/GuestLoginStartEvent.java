package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class GuestLoginStartEvent {

    public final boolean initiatedByUser;

    public GuestLoginStartEvent(boolean initiatedByUser) {
        this.initiatedByUser = initiatedByUser;
    }

}
