package common;

public record Link(Port p1, Port p2) {
	public Link(Port p1, Port p2) {
		assert p1.isAvailable() : "Port " + p1.toStr() + " unavailable";
		assert p2.isAvailable() : "Port " + p2.toStr() + " unavailable";
		
		p1.makeUnavailable(this);
		p2.makeUnavailable(this);
		
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public void passPacket(Packet p, Port origin) {
		assert p != null;
		assert p.head() != null;
		assert p.head().dest() != null;
		assert p.head().src() != null;
		
		if (p1 == origin) {
			p2.owner().acceptPacket(p, p2);
		}
		if (p2 == origin) {
			p1.owner().acceptPacket(p, p1);
		}
	}
	
	public void remove() {
		p1.makeAvailable();
		p2.makeAvailable();
	}
}
