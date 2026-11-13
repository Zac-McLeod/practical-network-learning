package common;

public class Packet {
	private final Header head;
	private final byte[] body;
	
	public Packet(Header head, byte[] body) {
		assert head != null;
		assert body != null;
		
		this.head = head;
		this.body = body;
	}
	
	public Header head() { return this.head; }
	public byte[] body() { return this.body; }
}