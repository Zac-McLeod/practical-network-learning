package common;

public class Port {
	private final int num;
	private final Node owner;
	private boolean isAvailable = true;

	public Port(int portNum, Node owner) { 
		assert portNum >= 0;
		assert owner != null;
		
		this.num = portNum;
		this.owner = owner;
	}
	
	public boolean isAvailable() { return this.isAvailable; }
	public int portNum() { return this.num; }
	public Node owner() { return this.owner; }
	
	public void makeUnavailable(Link l) { 
		assert this.isAvailable : this.toStr() + " already in use";
		assert l != null;
		
		owner.link(this, l);
		this.isAvailable = false;
	}
	
	public void makeAvailable() {
		assert !this.isAvailable : this.toStr() + " is already available";
		
		owner.free(this);
		this.isAvailable = true;
	}
	
	public String toStr() { return this.owner.toStr() + ":" + this.num; }
}
