package com.example.administrator.alshow.test;


import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.Motor;
import com.example.administrator.alshow.model.PositiveBar;
import com.example.administrator.alshow.service.MyService;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class MyTest{

    private MyService service=new MyService();


    @Test
    public void testGetGroove(){
        Groove groove=service.getGroove(1);
        assertEquals(1,groove.getBarsOfA().size());
        assertEquals(1,groove.getBarsOfB().size());
    }

    @Test
    public void testGetBar(){
        PositiveBar bar=service.getPositiveBar(1,1,false);
        assertEquals(19,bar.getCurrent(),0);
        assertEquals(19,bar.getVoltage(),0);
        assertEquals(19,bar.getTempareture(),0);
        assertEquals(2,bar.getMotor().getSpeed());
    }

    @Test
    public void testMotor(){
        Motor motor=service.getMotor(1);
        assertEquals(1,motor.getSpeed());
    }
}
