package common;

import java.util.ArrayList;
import java.util.Random;

public class MACAddr {
	private final String addrString;
	
	public MACAddr() {
		StringBuilder addrString = new StringBuilder();
		Random random = new Random();
	
		for (int i=0; i<6; i++){
			int octet = random.nextInt(256);
			addrString.append(String.format("%02X", octet));
			if (i<5) addrString.append(":");
		}
		this.addrString = addrString.toString();
	}

	public final String toStr() { return this.addrString; }
}