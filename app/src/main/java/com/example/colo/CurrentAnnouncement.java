package com.example.colo;

import androidx.appcompat.app.AppCompatActivity;

public class CurrentAnnouncement{

    String title;
    String description;
    String createdTime;

    private CurrentAnnouncement() {}

    public CurrentAnnouncement(String title, String description, String createdTime) {
        this.title = title;
        this.description = description;
        this.createdTime = createdTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedTime() {
        return createdTime;
    }
}
