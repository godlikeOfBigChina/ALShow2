package com.example.administrator.alshow.model;

import com.example.administrator.alshow.service.MotorDriver;

public abstract class Motor{
    private int id;
    private boolean isA;
    private boolean reach;
    private MotorDriver driver;

    
    public Motor(int id, boolean isA) {
		this.id = id;
		this.isA = isA;
		this.driver=new MotorDriver();
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isReach() {
        return reach;
    }

    public void setReach(boolean reach) {
        this.reach = reach;
    }

    public boolean isA() {
		return isA;
	}

	public void setA(boolean isA) {
		this.isA = isA;
	}
	

	public MotorDriver getDriver() {
		return driver;
	}

	public void setDriver(MotorDriver driver) {
		this.driver = driver;
	}

	public abstract boolean forward() throws Exception;
    
    public abstract boolean backward() throws Exception;

}
