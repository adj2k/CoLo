package com.example.colo;

import android.app.Application;

public class GlobalLoginInfo extends Application {
    private String loginEmail;
    private String loginPassword;

    public String getloginEmail() {
        return loginEmail;
    }

    public void setloginEmail(String email) {
        this.loginEmail = email;
    }


    public String getloginPassword() {
        return loginPassword;
    }

    public void setloginPassword(String password) {
        this.loginPassword = password;
    }

}
