package com.nbsg;

import lombok.Data;

@Data
public class Notification {
    private String dateTime;
    private String title;

    public Notification(String dateTime, String title) {
        this.dateTime = dateTime;
        this.title = title;
    }
}
