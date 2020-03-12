package com.ofidy.ofidyshoppingbrowser.Materials.Events;

public class RegisterStartEvent {

    public final String email;
    public final String password;
    public final String firstName;
    public final String lastName;
    public final String userName;
    public final String memword;
    public final String day;
    public final String month;
    public final String year;
    public final String gender;
    public final String telephone;
    public final String mobile;
    public final String currency;

    public RegisterStartEvent(String email, String password, String firstName, String lastName, String userName,
                              String memword, String day, String month, String year, String gender, String telephone,
                              String mobile, String currency) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.memword = memword;
        this.day = day;
        this.month = month;
        this.year = year;
        this.gender = gender;
        this.telephone = telephone;
        this.mobile = mobile;
        this.currency = currency;
    }

}
