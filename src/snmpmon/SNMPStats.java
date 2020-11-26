package snmpmon;

import java.io.IOException;

import com.ireasoning.protocol.snmp.*;


public class SNMPStats {
	
	private SnmpSession sessionR1, sessionR2, sessionR3;
	private SnmpTarget targetR1, targetR2, targetR3;
	private int port;
	private String ipR1, ipR2, ipR3, com;
	
	public SNMPStats(String ip1, String ip2, String ip3) throws IOException{
		ipR1 = ip1;
		ipR2 = ip2;
		ipR3 = ip3;
		com = "si2019";
		port = 161;
		
		targetR1 = new SnmpTarget(ipR1, port, com, com);
		sessionR1 = new SnmpSession(targetR1);
		
		targetR2 = new SnmpTarget(ipR2, port, com, com);
		sessionR2 = new SnmpSession(targetR2);
		
		targetR3 = new SnmpTarget(ipR3, port, com, com);
		sessionR3 = new SnmpSession(targetR3);
		
		
	}
	
	public void sendBadCommunity(int r) throws IOException {
		switch(r) {
		case 1:
			targetR1.setReadCommunity("random");
			sessionR1 = new SnmpSession(targetR1);
			sessionR1.asyncSnmpGetRequest(".1.3.6.1.2.1.11.1.0");
			targetR1.setReadCommunity(com);
			sessionR1 = new SnmpSession(targetR1);
			break;
		case 2:
			targetR2.setReadCommunity("random");
			sessionR2 = new SnmpSession(targetR2);
			sessionR2.asyncSnmpGetRequest(".1.3.6.1.2.1.11.1.0");
			targetR2.setReadCommunity(com);
			sessionR2 = new SnmpSession(targetR2);
			break;
		case 3:
			targetR3.setReadCommunity("random");
			sessionR3 = new SnmpSession(targetR3);
			sessionR3.asyncSnmpGetRequest(".1.3.6.1.2.1.11.1.0");
			targetR3.setReadCommunity(com);
			sessionR3 = new SnmpSession(targetR3);
			break;
		default:
			System.out.println("Incorrect router number");
		}
	}
	
	public SnmpPdu getINPackets(int router) throws IOException{
		switch(router) {
		case 1:
			return sessionR1.snmpGetRequest(".1.3.6.1.2.1.11.1.0");
		case 2:
			return sessionR2.snmpGetRequest(".1.3.6.1.2.1.11.1.0");
		case 3:
			return sessionR3.snmpGetRequest(".1.3.6.1.2.1.11.1.0");
		default:
			System.out.println("Invalid router number");
			break;
		}
		return null;
	}
	
	public SnmpPdu getOUTPackets(int router) throws IOException{
		switch(router) {
		case 1:
			return sessionR1.snmpGetRequest(".1.3.6.1.2.1.11.2.0");
		case 2:
			return sessionR2.snmpGetRequest(".1.3.6.1.2.1.11.2.0");
		case 3:
			return sessionR3.snmpGetRequest(".1.3.6.1.2.1.11.2.0");
		default:
			System.out.println("Invalid router number");
			break;
		}
		return null;
	}
	
	public SnmpPdu getGETRequests(int router) throws IOException{
		switch(router) {
		case 1:
			return sessionR1.snmpGetRequest(".1.3.6.1.2.1.11.15.0");
		case 2:
			return sessionR2.snmpGetRequest(".1.3.6.1.2.1.11.15.0");
		case 3:
			return sessionR3.snmpGetRequest(".1.3.6.1.2.1.11.15.0");
		default:
			System.out.println("Invalid router number");
			break;
		}
		return null;
	}
	
	public SnmpPdu getSETRequests(int router) throws IOException{
		switch(router) {
		case 1:
			return sessionR1.snmpGetRequest(".1.3.6.1.2.1.11.17.0");
		case 2:
			return sessionR2.snmpGetRequest(".1.3.6.1.2.1.11.17.0");
		case 3:
			return sessionR3.snmpGetRequest(".1.3.6.1.2.1.11.17.0");
		default:
			System.out.println("Invalid router number");
			break;
		}
		return null;
	}
	
	
	public SnmpPdu getNumTraps(int router) throws IOException{
		switch(router) {
		case 1:
			return sessionR1.snmpGetRequest(".1.3.6.1.2.1.11.29.0");
		case 2:
			return sessionR2.snmpGetRequest(".1.3.6.1.2.1.11.29.0");
		case 3:
			return sessionR3.snmpGetRequest(".1.3.6.1.2.1.11.29.0");
		default:
			System.out.println("Invalid router number");
			break;
		}
		return null;
	}
	
	public void testSET(int router) throws IOException{
		SnmpVarBind a = null;
		switch(router) {
		case 1:
			sessionR1.snmpSetRequest(a);
			return;
		case 2:
			sessionR2.snmpSetRequest(a);
			return;
		case 3:
			sessionR3.snmpSetRequest(a);
			return;
		default:
			System.out.println("Invalid router number");
			break;
		}
	}
	
	public SnmpPdu getNumBadCommunity(int router) throws IOException{
		switch(router) {
		case 1:
			return sessionR1.snmpGetRequest(".1.3.6.1.2.1.11.4.0");
		case 2:
			return sessionR2.snmpGetRequest(".1.3.6.1.2.1.11.4.0");
		case 3:
			return sessionR3.snmpGetRequest(".1.3.6.1.2.1.11.4.0");
		default:
			System.out.println("Invalid router number");
			break;
		}
		return null;
	}
	
}
