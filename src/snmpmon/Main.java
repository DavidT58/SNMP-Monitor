package snmpmon;

import java.io.IOException;

import com.ireasoning.protocol.snmp.*;

public class Main {
	
	
	public static void main(String[] args) throws IOException {
		SNMPStats table = new SNMPStats("192.168.10.1", "192.168.20.1", "192.168.30.1");
		
		//MibUtil.loadMib2();
		//System.out.println(MibUtil.translateOID("1.3.6.1.4.1", true));
		
		/*
		for(int i = 0; i < 3; i++) {
			table.testSET(i+1);
		}
		*/
		
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getGETRequests(i+1).getFirstVarBind().getValue();
			System.out.println("Number of GET requests on Router" + (i+1) + ": " + val);
		}
		
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
	}
}
