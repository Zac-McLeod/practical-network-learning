package common;

/// Links represent an L1 connection between nodes
/// Links are bidirectional
/// Links connect exactly one port on one node, to exactly one other port on another node
/// Links pass packets between nodes
/// Links manage their own connection to ports on nodes
public record Link(Port p1, Port p2) {
	
	/// @param p1 Port of one node to connect to
	/// @param p2 Port of one node to connect to
	/// @return Link
	/// @throws AssertionError
	public Link(Port p1, Port p2) {
		assert p1 != null;
		assert p2 != null;
		assert p1 != p2;
		assert p1.owner() != p2.owner();
		assert p1.isAvailable() : "Port " + p1.toStr() + " unavailable";
		assert p2.isAvailable() : "Port " + p2.toStr() + " unavailable";
		
		p1.makeUnavailable(this);
		p2.makeUnavailable(this);
		
		this.p1 = p1;
		this.p2 = p2;
	}
	
	/// Transport packet along connection
	/// @param packet Packet to transport
	/// @param origin Port of origin
	/// @throws AssertionError
	public void passPacket(Packet packet, Port origin) {
		assert packet != null;
		assert packet.head() != null;
		assert packet.head().dest() != null;
		assert packet.head().src() != null;
		
		if (p1 == origin) {
			p2.owner().acceptPacket(packet, p2);
		}
		if (p2 == origin) {
			p1.owner().acceptPacket(packet, p1);
		}
	}
	
	/// Update port owner mappings that link no longer exists
	public void remove() {
		p1.makeAvailable();
		p2.makeAvailable();
	}
}
