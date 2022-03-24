package com.example.colo.Announcements;

import java.util.ArrayList;

public class AnnouncementHelperClass {
    String aTitle, aDescription;

    public AnnouncementHelperClass(String aTitle, String aDescription) {
        this.aTitle = aTitle;
        this.aDescription = aDescription;
    }

    public String getaTitle() {
        return aTitle;
    }

    public String getaDescription() {
        return aDescription;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle;
    }

    public void setaDescription(String aDescription) {
        this.aDescription = aDescription;
    }

}
