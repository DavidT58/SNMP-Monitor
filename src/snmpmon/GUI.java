package snmpmon;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.ireasoning.protocol.snmp.*;
//import org.jfree.*;


public class GUI extends Thread {
	private JFrame frame;
	private Panel centerPanel;
	private Label lastUpdate;
	private Label R1, R2, R3;
	private JTable table;
	private DefaultTableModel model;
	private SNMPStats stats;
	
	public GUI() throws IOException{
		frame = new JFrame("SNMP Monitor");
		stats = new SNMPStats("192.168.10.1", "192.168.20.1", "192.168.30.1");
		model = new DefaultTableModel(4, 7);
		table = new JTable(model);
		setupTable();
		
		frame.add(table);
		
		this.start();
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 300);
		frame.setVisible(true);
	}
	
	private void setupTable() {
		model.setValueAt("Router", 0, 0);
		model.setValueAt("Router1", 1, 0);
		model.setValueAt("Router2", 2, 0);
		model.setValueAt("Router3", 3, 0);
		model.setValueAt("In Packets", 0, 1);
		model.setValueAt("Out Packets", 0, 2);
		model.setValueAt("GET Requests", 0, 3);
		model.setValueAt("SET Requests", 0, 4);
		model.setValueAt("TRAPS", 0, 5);
		model.setValueAt("BAD COMMUNITY", 0, 6);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(30);
		
		
	}
	
	public void run() {
		try {
			while(!isInterrupted()) {
				updateValues();
				table.repaint();
				Thread.sleep(5000);
			}
		} catch (InterruptedException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	for(int i = 0; i < 3; i++) {
		table.testSET(i+1);
	}
	*/
	
	private void updateValues() throws IOException{
		/*
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = stats.getGETRequests(i+1).getFirstVarBind().getValue();
			System.out.println("Number of GET requests on Router" + (i+1) + ": " + val);
			model.setValueAt(val, i+1, 3);
		}
		*/
		
		for(int i = 1; i < 4; i++) {
			model.setValueAt(stats.getINPackets(i).getFirstVarBind().getValue(), i, 1);
			model.setValueAt(stats.getOUTPackets(i).getFirstVarBind().getValue(), i, 2);
			
			model.setValueAt(stats.getGETRequests(i).getFirstVarBind().getValue(), i, 3);
			model.setValueAt(stats.getSETRequests(i).getFirstVarBind().getValue(), i, 4);
			
			model.setValueAt(stats.getNumTraps(i).getFirstVarBind().getValue(), i, 5);
			model.setValueAt(stats.getNumBadCommunity(i).getFirstVarBind().getValue(), i, 6);
			
		}
	}
	
	
}
