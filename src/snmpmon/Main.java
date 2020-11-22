package snmpmon;

import java.io.IOException;

import com.ireasoning.protocol.snmp.*;

public class Main {
	
	
	public static void main(String[] args) throws IOException {
		SNMPStats table = new SNMPStats("192.168.10.1", "192.168.20.1", "192.168.30.1");
		
		//MibUtil.loadMib2();
		/*
		for(int i = 0; i < table.getLPAttr(3).length; i++) {
			SnmpOID name = table.getLPAttr(3)[i].getName();
			SnmpDataType val = table.getLPAttr(3)[i].getValue();
			String ipAddress = name.get(10) + "." + "" + name.get(11) + "." + name.get(12) + "." + name.get(13);
			String ipAddressSource = name.get(15) + "." + "" + name.get(16) + "." + name.get(17) + "." + name.get(18);
			System.out.println(ipAddress + " LP: " + val + " via " + ipAddressSource);
		}
		*/
		
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getINPackets(i+1).getFirstVarBind().getValue();
			System.out.println("Number of IN packets on Router" + (i+1) + ": " + val);
		}
		
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getOUTPackets(i+1).getFirstVarBind().getValue();
			System.out.println("Number of OUT packets on Router" + (i+1) + ": " + val);
		}
		
		for(int i = 0; i < 3; i++) {
			SnmpDataType val = table.getGETRequests(i+1).getFirstVarBind().getValue();
			System.out.println("Number of GET requests on Router" + (i+1) + ": " + val);
		}
	}
}
