package com.ofidy.ofidyshoppingbrowser.Materials.Events;

//Event class to begin login action
public class LoginStartEvent {

    public final String email;
    public final String password;
    //If event is initiated by user or automatically called
    public final boolean initiatedByUser;

    public LoginStartEvent(String email, String password, boolean initiatedByUser) {
        this.email = email;
        this.password = password;
        this.initiatedByUser = initiatedByUser;
    }

}
