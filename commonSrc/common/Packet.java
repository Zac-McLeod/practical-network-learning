package common;

/// Packets can be sent from node to node
/// Packet bodies are only read by Hosts
/// Packets can currently have any size body - will be changed when IP is implemented
public class Packet {
	private final Header head;
	private final byte[] body;
	
	/// Representation of a packet
	/// @param head Packet header
	/// @param body Packet contents
	/// @return Packet
	/// @throws AssertionError
	public Packet(Header head, byte[] body) {
		assert head != null;
		assert body != null;
		
		this.head = head;
		this.body = body;
	}
	
	public Header head() { return this.head; }
	public byte[] body() { return this.body; }
}