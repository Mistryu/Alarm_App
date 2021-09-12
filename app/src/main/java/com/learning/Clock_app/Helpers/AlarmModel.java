package com.learning.Clock_app.Helpers;

public class AlarmModel {

    private int id;
    private int hour;
    private int minutes;
    private String label;
    private String days;
    private boolean isActive;

    public AlarmModel(int id, int hour, int minutes, String label, String days, boolean isActive) {
        this.id = id;
        this.hour = hour;
        this.minutes = minutes;
        this.label = label;
        this.days = days;
        this.isActive = isActive;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minutes;
    }

    public void setMinute(int minute) {
        this.minutes = minute;
    }
}
