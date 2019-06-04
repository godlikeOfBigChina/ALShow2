package com.example.administrator.alshow.model;

public class AlertDiary {
    private String numberOfBar;
    private String occurTime;
    private int alertClass;
    private String description;

    public String getNumberOfBar() {
        return numberOfBar;
    }

    public void setNumberOfBar(String numberOfBar) {
        this.numberOfBar = numberOfBar;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public int getAlertClass() {
        return alertClass;
    }

    public void setAlertClass(int alertClass) {
        this.alertClass = alertClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
