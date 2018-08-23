package com.example.administrator.alshow.model;

import java.util.List;

public class Groove {
    private int id;
    private List<PositiveBar> barsOfA;
    private List<PositiveBar> barsOfB;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<PositiveBar> getBarsOfA() {
        return barsOfA;
    }

    public void setBarsOfA(List<PositiveBar> barsOfA) {
        this.barsOfA = barsOfA;
    }

    public List<PositiveBar> getBarsOfB() {
        return barsOfB;
    }

    public void setBarsOfB(List<PositiveBar> barsOfB) {
        this.barsOfB = barsOfB;
    }
}
