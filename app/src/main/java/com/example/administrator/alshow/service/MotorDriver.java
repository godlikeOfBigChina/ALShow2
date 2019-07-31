package com.example.administrator.alshow.service;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.example.administrator.alshow.model.Configuration;

public class MotorDriver {

	private Logger log;
	private Socket client;
	private String serverIp;
	private int portNum;
	private Configuration config;
	
	
	public MotorDriver() {
		this.config=new Configuration();
		this.serverIp = config.ip;
		this.portNum=config.port;
		log=Logger.getLogger("MotorDriver");
	}
	
	
	public boolean drive(boolean isSingle,boolean isBoth,boolean isA,boolean isOut,int barNum) throws Exception {
		byte[] cmd=null;
		if(isSingle) {
			cmd=updatePnFnCR(isA, isOut, barNum);
		}else {
			cmd=getByteData(isBoth, isA, isOut);
		}
		return send(cmd);
	}
	
	private boolean send(byte[] cmdData) throws UnknownHostException, IOException {
		client=new Socket(this.serverIp,this.portNum);
		client.setSoTimeout(50000);
		client.getOutputStream().write(cmdData);
		log.info("send!!!");
		byte[] data=new byte[1024];
		client.getInputStream().read(data);
		if(data.length>0) {
			log.info("recieved data:"+int2byte16(data[0]));
		}
		client.close();
		if(data[0]==0x68) {
			return true;
		}else {
			return false;
		}
	}
	
	private byte[] getByteData(boolean ifBoth,boolean ifA,boolean isOut){
		byte[] cmd=null;
		if(ifBoth) {
			if(isOut) {
				cmd=config.allOut;
			}else{
				cmd=config.allIn;
			}
		}else {
			String situation="";
			situation+=ifA?"1":"0";
			situation+=isOut?"1":"0";
			switch (situation) {
			case "00":
				cmd=config.allBIn;
				break;
			case "10":
				cmd=config.allAIn;
				break;
			case "01":
				cmd=config.allBOut;
				break;
			case "11":
				cmd=config.allAOut;
				default:
			}
		}
		return cmd;
	}
	
	private byte[] updatePnFnCR(boolean isA,boolean isOut,int barNum) throws Exception {
		byte[] data=config.single;
		int fn;
		if(isA){
			data[14]=(byte)0x0A;
			fn=48+barNum;
		}else {
			data[14]=(byte)0x0B;
			fn=72+barNum;
		}
		if(barNum>0&&barNum<=24) {
			data[15]=int2byte16(barNum);
		}else {
			throw new Exception("number of positivebar does not exist");
		}
		log.info("fn is "+data[15]+"");
		data[16]=int2byte16((int)Math.pow(2,(fn-1)%8));
		data[17]=int2byte16((fn-1)/8);
		
		if(isOut) {
			data[18]=(byte) 0xFF;
		}else {
			data[18]=0x00;
		}
		byte sum=0;
		for(int i=6;i<=18;i++) {
			sum+=data[i];
		}
		log.info("sum is "+Integer.toHexString(sum));
		sum%=0xFF;
		data[19]=int2byte16(sum);
		return data;
	}
	
	private byte int2byte16(int x) {
		return (byte)(0xFF&x);
	}
}
