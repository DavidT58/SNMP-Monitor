package snmpmon;

import java.io.IOException;

import com.ireasoning.protocol.snmp.*;

public class BGPTable {
	
	private SnmpSession session;
	private SnmpTarget target;
	private int port;
	private String ip, rdc, wrc;
	
	public BGPTable(String ipa) throws IOException{
		ip = ipa;
		rdc = wrc = "si2019";
		port = 161;
		target = new SnmpTarget(ip, port, rdc, wrc);
		session = new SnmpSession(target);
	}
	
	public SnmpVarBind[] getLocalPreferenceAttribute() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.12");
	}
	
	public static void main(String[] args) throws IOException {
		BGPTable table = new BGPTable("192.168.30.1");
		System.out.println(table.getLocalPreferenceAttribute()[0]);
		
		

	}
	
}
