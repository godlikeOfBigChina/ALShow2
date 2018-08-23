package com.example.administrator.alshow.model;

public class PositiveBar {
    private int grooveId;
    private int id;
    private boolean ifA;
    private float voltage;
    private float current;
    private float tempareture;
    private Motor motor;

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public int getGrooveId() {
        return grooveId;
    }

    public void setGrooveId(int grooveId) {
        this.grooveId = grooveId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIfA() {
        return ifA;
    }

    public void setIfA(boolean ifA) {
        this.ifA = ifA;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getTempareture() {
        return tempareture;
    }

    public void setTempareture(float tempareture) {
        this.tempareture = tempareture;
    }

}
