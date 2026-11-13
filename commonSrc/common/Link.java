package common;

public record Link(Port p1, Port p2, Protocol type) {
	public Link(Port p1, Port p2, Protocol type) {
		assert p1.isAvailable() : "Port " + p1.toStr() + " unavailable";
		assert p2.isAvailable() : "Port " + p2.toStr() + " unavailable";
		
		p1.makeUnavailable(this);
		p2.makeUnavailable(this);
		
		this.p1 = p1;
		this.p2 = p2;
		this.type = type;
	}
	
	public void passPacket(Packet p) {
		assert p != null;
		assert p.head() != null;
		assert p.head().dest() != null;
		assert p.head().src() != null;
		
		MACAddr src = p.head().src();
		
		if (p1.owner().mac() == src) {
			p2.owner().acceptPacket(p, p2);
		}
		if (p2.owner().mac() == src) {
			p1.owner().acceptPacket(p, p1);
		}
	}
	
	public void remove() {
		p1.makeAvailable();
		p2.makeAvailable();
	}
}
