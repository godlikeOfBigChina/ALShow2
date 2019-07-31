package com.example.administrator.alshow.test;


import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.OpDiary;
import com.example.administrator.alshow.model.PositiveBar;
import com.example.administrator.alshow.service.MyService;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MyTest{

    private MyService service=new MyService();


    @Test
    public void testGetGroove(){
        Groove groove=service.getGroove(1001);
        assertEquals(24,groove.getBarsOfA().size());
        assertEquals(24,groove.getBarsOfB().size());
    }

    @Test
    public void testGetBar(){
        List<PositiveBar> bars=service.getPositiveBarHistory(1001,true,1);
        assertEquals(21,bars.size(),0);
    }

    @Test
    public void testReadLog(){
        List<OpDiary> logs=service.getReadLog("ycn");
        assertEquals(2,logs.size(),0);
    }

    @Test
    public void testWriteLog(){
        OpDiary row=new OpDiary();
        row.setOpTime("20190731 15:33");
        row.setUsername("ycn");
        row.setOpType(1);
        row.setOpObject("");
        service.handleActionWriteLog(row);
    }
}
