package com.example.sw_soc;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class User {
    public String id;
    public String pw;
    public String name;
    public String phoneNumber;
    public String hint;

    public User() {

    }

    public User(String id, String pw, String name, String phoneNumber, String hint) {
        id = this.id;
        pw = this.pw;
        name = this.name;
        phoneNumber = this.phoneNumber;
        hint = this.hint;
    }

}
