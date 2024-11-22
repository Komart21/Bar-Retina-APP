package com.example.bar_retina_app;

public class Notification {
    private String message;
    private String date;

    public Notification(String message, String date) {
        this.message = message;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}
