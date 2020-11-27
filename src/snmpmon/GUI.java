package snmpmon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.ireasoning.protocol.snmp.*;
//import org.jfree.*;


public class GUI extends Thread {
	private JFrame frame;
	private Panel centerPanel, bottomPanel;
	private Label lastUpdate, currentLabel;
	private Button sendSET, changeTable, sendBadCommunity;
	private CardLayout cardLayout = new CardLayout();
	
	private JTable totalTable, currentTable;
	private DefaultTableModel modelTotal, modelCurrent;
	private JComboBox<String> routerSelection;
	
	private SNMPManager manager;
	private Date lastUpdateTime;
	private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	private int seconds = 2;
	
	
	private long oldValues[][], latestValues[][];
	
	public GUI() throws IOException{
		frame = new JFrame("SNMP Monitor");
		bottomPanel = new Panel(new FlowLayout());
		centerPanel = new Panel();
		
		
		manager = new SNMPManager("192.168.10.1", "192.168.20.1", "192.168.30.1");
		
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
		
		
		FlowLayout fl = new FlowLayout();
		fl.setVgap(40);
		frame.setLayout(fl);
		frame.add(centerPanel);
		frame.add(bottomPanel);
		
		
		this.start();
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 300);
		frame.setVisible(true);
	}
	
	private void setupCenterPanel() {
		centerPanel.setLayout(cardLayout);
		
		Label totalLabel;
		currentLabel = new Label ("Units/"+seconds+"s");
		totalLabel = new Label("Total");
		
		
		Panel total = new Panel();
		Panel current = new Panel();
		
		total.add(totalTable);
		current.add(currentTable);
		total.add(totalLabel);
		current.add(currentLabel);
		
		
		
		centerPanel.add(total);
		centerPanel.add(current);
		
	}
	
	private void setupBottomPanel() {
		String[] routers = {"R1", "R2", "R3"};
		routerSelection = new JComboBox<String>(routers);
		bottomPanel.add(routerSelection);
		
		String[] time = {"2s", "5s", "10s"};
		JComboBox<String> s = new JComboBox<String>(time);
		
		sendSET = new Button("Send SET");
		changeTable = new Button("Change Table");
		sendBadCommunity = new Button("Send Bad Community");
		
		lastUpdateTime = new Date(System.currentTimeMillis());
		lastUpdate = new Label("Last update: " + df.format(lastUpdateTime));
		
		bottomPanel.add(sendSET);
		bottomPanel.add(sendBadCommunity);
		bottomPanel.add(changeTable);
		bottomPanel.add(s);
		bottomPanel.add(lastUpdate);
		
		s.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switch(s.getSelectedIndex()) {
				case 0:
					seconds = 2;
					currentLabel.setText("Units/"+seconds+"s");
					break;
				case 1:
					seconds = 5;
					currentLabel.setText("Units/"+seconds+"s");
					break;
				case 2:
					seconds = 10;
					currentLabel.setText("Units/"+seconds+"s");
					break;
				}
			}
		});
		
		sendBadCommunity.addActionListener((a) -> {
			try {
				manager.sendBadCommunity(routerSelection.getSelectedIndex() + 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		
		changeTable.addActionListener((a) -> { 
			cardLayout.next(centerPanel);
			
		});
		
		sendSET.addActionListener((a) -> {
			try {
				manager.sendSET(routerSelection.getSelectedIndex() + 1);
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
				
				Thread.sleep(seconds*1000);
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
			modelTotal.setValueAt(manager.getINPackets(i).getFirstVarBind().getValue(), i, 1);
			modelTotal.setValueAt(manager.getOUTPackets(i).getFirstVarBind().getValue(), i, 2);
			
			modelTotal.setValueAt(manager.getGETRequests(i).getFirstVarBind().getValue(), i, 3);
			modelTotal.setValueAt(manager.getSETRequests(i).getFirstVarBind().getValue(), i, 4);
			
			modelTotal.setValueAt(manager.getNumTraps(i).getFirstVarBind().getValue(), i, 5);
			modelTotal.setValueAt(manager.getNumBadCommunity(i).getFirstVarBind().getValue(), i, 6);
			
			totalTable.repaint();
		}
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 6; j++)
				latestValues[i][j] = ((SnmpCounter32)modelTotal.getValueAt(i+1, j+1)).getValue();	
		}
		lastUpdateTime.setTime(System.currentTimeMillis());
		lastUpdate.setText("Last update: " + df.format(lastUpdateTime));
	}
	
	/*
	 * This method gets data from the latestValues matrix and shows how much has changed 
	 * over selected time period (should be 10s by default)
	 * 
	 */
	private void updateValuesCurrent() throws IOException {
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 6; j++) {
				modelCurrent.setValueAt((latestValues[i][j] - oldValues[i][j]), i+1, j+1);
				oldValues[i][j] = latestValues[i][j];
			}
			currentTable.repaint();
		}		
	}
}
