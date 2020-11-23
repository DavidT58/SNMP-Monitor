package snmpmon;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import com.ireasoning.protocol.snmp.SnmpDataType;

public class GUI extends Thread {
	private JFrame frame;
	private Panel centerPanel;
	private Label lastUpdate;
	
	public GUI() throws IOException{
		frame = new JFrame("SNMP Monitor");
		this.start();
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
	
	public void run() {
		try {
			while(!isInterrupted()) {
				Thread.sleep(5000);
				drawMonitor();
			}
		} catch (InterruptedException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void drawMonitor() throws IOException{
		
		SNMPStats table = new SNMPStats("192.168.10.1", "192.168.20.1", "192.168.30.1");
		
		
		
		/*
		for(int i = 0; i < 3; i++) {
			table.testSET(i+1);
		}
		*/
		
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getGETRequests(i+1).getFirstVarBind().getValue();
			System.out.println("Number of GET requests on Router" + (i+1) + ": " + val);
		}
		/*
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getSETRequests(i+1).getFirstVarBind().getValue();
			System.out.println("Number of SET requests on Router" + (i+1) + ": " + val);
		}
		
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getNumBadCommunity(i+1).getFirstVarBind().getValue();
			System.out.println("Number of Bad Community requests on Router" + (i+1) + ": " + val);
		}
		
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getNumTraps(i+1).getFirstVarBind().getValue();
			System.out.println("Number of Traps on Router" + (i+1) + ": " + val);
		}
		
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getINPackets(i+1).getFirstVarBind().getValue();
			System.out.println("Number of IN packets on Router" + (i+1) + ": " + val);
		}
		
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getOUTPackets(i+1).getFirstVarBind().getValue();
			System.out.println("Number of OUT packets on Router" + (i+1) + ": " + val);
		}
		*/
	}
	
	
}
