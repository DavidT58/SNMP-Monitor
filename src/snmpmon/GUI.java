package snmpmon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.ireasoning.protocol.snmp.*;
//import org.jfree.*;


public class GUI extends Thread {
	private JFrame frame;
	private Panel centerPanel, bottomPanel;
	private Label lastUpdate;
	private Button sendSET, changeTable, sendBadCommunity;
	private CardLayout cardLayout;
	
	
	private JTable totalTable, currentTable;
	private DefaultTableModel modelTotal, modelCurrent;
	private JComboBox<String> routerSelection;
	
	SNMPStats stats;
	
	
	private long oldValues[][], latestValues[][];
	
	public GUI() throws IOException{
		frame = new JFrame("SNMP Monitor");
		bottomPanel = new Panel(new FlowLayout());
		centerPanel = new Panel();
		cardLayout = new CardLayout();
		
		stats = new SNMPStats("192.168.10.1", "192.168.20.1", "192.168.30.1");
		
		oldValues = new long[3][6];
		latestValues = new long[3][6];
		
		// this model shows the overall total number of requests/traps/packets
		modelTotal = new DefaultTableModel(4, 7); 
		
		// this model shows the data monitored over the selected time period (data/10s)
		modelCurrent = new DefaultTableModel(4,7); 
		
		totalTable = new JTable(modelTotal);
		currentTable = new JTable(modelCurrent);
		
		setupCurrentTable();
		setupTotalTable();
		setupCenterPanel();
		setupBottomPanel();
		
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.add(centerPanel);
		
		this.start();
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 300);
		frame.setVisible(true);
	}
	
	private void setupCenterPanel() {
		centerPanel.setLayout(cardLayout);
		
		Label currentLabel, totalLabel;
		currentLabel = new Label ("current");
		totalLabel = new Label("total");
		
		
		Panel total = new Panel();
		Panel current = new Panel();
		total.add(totalTable);
		current.add(currentTable);
		total.add(totalLabel);
		current.add(currentLabel);
		
		centerPanel.add(totalTable);
		
		centerPanel.add(currentTable);
		
	}
	
	private void setupBottomPanel() {
		String[] routers = {"R1", "R2", "R3"};
		routerSelection = new JComboBox<String>(routers);
		bottomPanel.add(routerSelection, BorderLayout.WEST);
		
		sendSET = new Button("Send SET");
		changeTable = new Button("Change Table");
		sendBadCommunity = new Button("Send Bad Community");
		
		bottomPanel.add(sendSET);
		bottomPanel.add(sendBadCommunity);
		bottomPanel.add(changeTable, BorderLayout.EAST);
		
		sendBadCommunity.addActionListener((a) -> {
			int r = routerSelection.getSelectedIndex() + 1;
			try {
				stats.sendBadCommunity(r);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		
		changeTable.addActionListener((a) -> { 
			cardLayout.next(centerPanel);
			
		});
		
		sendSET.addActionListener((a) -> {
			int r = routerSelection.getSelectedIndex() + 1;
			try {
				stats.testSET(r);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	private void setupTotalTable() {
		modelTotal.setValueAt("Router", 0, 0);
		modelTotal.setValueAt("Router1", 1, 0);
		modelTotal.setValueAt("Router2", 2, 0);
		modelTotal.setValueAt("Router3", 3, 0);
		modelTotal.setValueAt("In Packets", 0, 1);
		modelTotal.setValueAt("Out Packets", 0, 2);
		modelTotal.setValueAt("GET Requests", 0, 3);
		modelTotal.setValueAt("SET Requests", 0, 4);
		modelTotal.setValueAt("TRAPS", 0, 5);
		modelTotal.setValueAt("BAD COMMUNITY", 0, 6);
		
		totalTable.setRowHeight(30);
		totalTable.setDefaultEditor(Object.class, null);
		
	}
	
	
	private void setupCurrentTable() {
		currentTable.setRowHeight(30);
		currentTable.setDefaultEditor(Object.class, null);
		
		modelCurrent.setValueAt("Router", 0, 0);
		modelCurrent.setValueAt("Router1", 1, 0);
		modelCurrent.setValueAt("Router2", 2, 0);
		modelCurrent.setValueAt("Router3", 3, 0);
		modelCurrent.setValueAt("In Packets", 0, 1);
		modelCurrent.setValueAt("Out Packets", 0, 2);
		modelCurrent.setValueAt("GET Requests", 0, 3);
		modelCurrent.setValueAt("SET Requests", 0, 4);
		modelCurrent.setValueAt("TRAPS", 0, 5);
		modelCurrent.setValueAt("BAD COMMUNITY", 0, 6);
	}
	
	
	public void run() {
		try {
			while(!isInterrupted()) {
				
				updateValuesTotal();
				
				updateValuesCurrent();
				
				Thread.sleep(2000);
			}
		} catch (InterruptedException | IOException e) {
			
		}
		
	}
	
	
	/*
	 * This method pulls the data over SNMP via GET requests and stores it 
	 * into the latestValues matrix
	 */
	private void updateValuesTotal() throws IOException{
		
		for(int i = 1; i < 4; i++) {
			modelTotal.setValueAt(stats.getINPackets(i).getFirstVarBind().getValue(), i, 1);
			modelTotal.setValueAt(stats.getOUTPackets(i).getFirstVarBind().getValue(), i, 2);
			
			modelTotal.setValueAt(stats.getGETRequests(i).getFirstVarBind().getValue(), i, 3);
			modelTotal.setValueAt(stats.getSETRequests(i).getFirstVarBind().getValue(), i, 4);
			
			modelTotal.setValueAt(stats.getNumTraps(i).getFirstVarBind().getValue(), i, 5);
			modelTotal.setValueAt(stats.getNumBadCommunity(i).getFirstVarBind().getValue(), i, 6);
			
			totalTable.repaint();
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 6; j++)
				latestValues[i][j] = ((SnmpCounter32)modelTotal.getValueAt(i+1, j+1)).getValue();
			
		}
	}
	
	/*
	 * This method gets data from the latestValues matrix and shows how much has changed 
	 * over selected time period (should be 10s by default)
	 * 
	 */
	private void updateValuesCurrent() throws IOException {
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 6; j++)
				modelCurrent.setValueAt((latestValues[i][j] - oldValues[i][j]), i+1, j+1);
			
			currentTable.repaint();
		}
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 6; j++)
				oldValues[i][j] = latestValues[i][j];
	}
	
}
