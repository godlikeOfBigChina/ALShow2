package com.example.administrator.alshow.model;

public class Motor implements MotorControler {
    private int id;
    private int speed;
    private int steps;
    private int currents;
    private int acceleration;
    private int position;
    private int alertStatue;
    private int rounds;
    private int backSpeed;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getCurrents() {
        return currents;
    }

    public void setCurrents(int currents) {
        this.currents = currents;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getAlertStatue() {
        return alertStatue;
    }

    public void setAlertStatue(int alertStatue) {
        this.alertStatue = alertStatue;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getBackSpeed() {
        return backSpeed;
    }

    public void setBackSpeed(int backSpeed) {
        this.backSpeed = backSpeed;
    }

    @Override
    public int convert() {
        return 0;
    }

    @Override
    public boolean revert() {
        return false;
    }
}
