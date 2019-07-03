package com.example.administrator.alshow.model;

public class OpDiary {
    private String username;
    private String opTime;
    private int opType;
    private String opObject;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getOpObject() {
        return opObject;
    }

    public void setOpObject(String opObject) {
        this.opObject = opObject;
    }
}
