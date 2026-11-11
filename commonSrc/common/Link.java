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
	
	public void remove() {
		p1.makeAvailable();
		p2.makeAvailable();
	}
}
