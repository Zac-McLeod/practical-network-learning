package common;

import java.util.Random;

/// MAC addresses are randomly generated when a node is created
/// Not technically an accurate simulation of Real MAC addresses
/// Real MAC addresses are centrally allocated truly unique identifiers
/// For a small scale sim, this should suffice, although technically two nodes could have the same MAC
public class MACAddr{
	private final String addrString;
	
	/// Generates a new MAC address in MAC Address format
	public MACAddr() {
		StringBuilder addrString = new StringBuilder();
		Random random = new Random();
	
		// Randomly generate all 6 MAC Octets
		for (int i=0; i<6; i++){
			int octet = random.nextInt(256);
			addrString.append(String.format("%02X", octet));
			if (i<5) addrString.append(":");
		}
		this.addrString = addrString.toString();
	}

	public final String toStr() { return this.addrString; }
}