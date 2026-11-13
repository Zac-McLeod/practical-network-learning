package common;

/// Header for packets
/// Currently modeled after Ethernet packet headers, but will likely be extended to properly support IP
public class Header{
	private final MACAddr src;
	private final MACAddr dest;
	private final int ttl;
	private final Protocol type;
	
	/// Headers must have a source, destination, time to live, and protocol
	/// Headers can be created with AT LEAST a source and destination
	/// @param src MAC address of packet source
	/// @param dest MAC address of packet destination
	/// @param ttl Time To Live of packet
	/// @param type Protocol type of packet
	/// @return Packet header
	/// @throws AssertionError
	private Header(MACAddr src, MACAddr dest, int ttl, Protocol type) {
		assert src != null;
		assert dest != null;
		assert src != dest;
		assert ttl >= 0;
		assert type != null;
		
		this.src = src;
		this.dest = dest;
		this.ttl = ttl;
		this.type = type;
	}
	public static final Header of(MACAddr src, MACAddr dest) {
		return new Header(src, dest, 60, Protocol.ETH);
	}
	public static final Header of(MACAddr src, MACAddr dest, int ttl) {
		return new Header(src, dest, ttl, Protocol.ETH);
	}
	public static final Header of(MACAddr src, MACAddr dest, Protocol type) {
		return new Header(src, dest, 60, type);
	}
	public static final Header of(MACAddr src, MACAddr dest, int ttl, Protocol type) {
		return new Header(src, dest, ttl, Protocol.ETH);
	}
	
	public MACAddr src() { return this.src; }
	public MACAddr dest() { return this.dest; }
	public int ttl() { return this.ttl; }
	public Protocol type() { return this.type; }
	
	public String toStr() {
		return src.toStr() +" -> "+dest.toStr();
	}
}