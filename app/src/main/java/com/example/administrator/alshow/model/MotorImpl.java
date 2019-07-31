package com.example.administrator.alshow.model;

public class MotorImpl extends Motor {
	

	public MotorImpl(int id, boolean isA) {
		super(id, isA);
	}

	@Override
	public boolean forward() throws Exception {
		boolean rtv=this.getDriver().drive(true, false, this.isA(), true, this.getId());
		this.setReach(rtv);
		return rtv;
	}

	@Override
	public boolean backward() throws Exception {
		boolean rtv=this.getDriver().drive(true, false, this.isA(), false, this.getId());
		this.setReach(rtv);
		return rtv;
	}

}
