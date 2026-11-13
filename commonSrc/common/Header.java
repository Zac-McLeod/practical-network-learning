package common;

public class Header{
	private final MACAddr src;
	private final MACAddr dest;
	private final int ttl;
	private final Protocol type;
	
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