package com.example.colo;

import android.app.Application;

public class GlobalCompanyName extends Application {

    private String globalCompanyName;

    public String getGlobalCompanyName() {
        return globalCompanyName;
    }

    public void setGlobalCompanyName(String companyName) {
        this.globalCompanyName = companyName;
    }
}

