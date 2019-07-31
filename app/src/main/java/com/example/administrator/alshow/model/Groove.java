package com.example.administrator.alshow.model;

import java.util.List;

public class Groove {
    private int potNo;
    private String potName;
    private String ipAddress;
    private String readerNo;
    private String measureTable;
    private String hMeasureTable;

    private List<PositiveBar> barsOfA;
    private List<PositiveBar> barsOfB;

    public int getPotNo() {
        return potNo;
    }

    public void setPotNo(int potNo) {
        this.potNo = potNo;
    }

    public String getPotName() {
        return potName;
    }

    public void setPotName(String potName) {
        this.potName = potName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReaderNo() {
        return readerNo;
    }

    public void setReaderNo(String readerNo) {
        this.readerNo = readerNo;
    }

    public String getMeasureTable() {
        return measureTable;
    }

    public void setMeasureTable(String measureTable) {
        this.measureTable = measureTable;
    }

    public String gethMeasureTable() {
        return hMeasureTable;
    }

    public void sethMeasureTable(String hMeasureTable) {
        this.hMeasureTable = hMeasureTable;
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
