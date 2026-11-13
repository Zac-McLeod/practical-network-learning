package common;

/// Ports are numbered for identification purposes
/// Ports know what node they belong to
/// Ports also know if they are in use by their parent node
/// Ports are the interface between nodes and links
/// Ports can only connect to ONE link at a time
public class Port {
	private final int num;
	private final Node owner;
	private boolean isAvailable = true;

	/// @param portNum Integer to identify port ordering for a node
	/// @param owner Node that port belongs to
	/// @return Port
	/// @throws AssertionError
	public Port(int portNum, Node owner) { 
		assert portNum >= 0;
		assert owner != null;
		
		this.num = portNum;
		this.owner = owner;
	}
	
	public boolean isAvailable() { return this.isAvailable; }
	public int portNum() { return this.num; }
	public Node owner() { return this.owner; }
	
	/// Called by Link object to tell port's owner to add link to internal mappings
	/// @param link Link object port is connecting to
	/// @throws AssertionError
	public void makeUnavailable(Link link) { 
		assert this.isAvailable : this.toStr() + " already in use";
		assert link != null;
		
		owner.link(this, link);
		this.isAvailable = false;
	}
	
	/// Called by Link object to tell port's owner to remove link from internal mappings
	/// @throws AssertionError
	public void makeAvailable() {
		assert !this.isAvailable : this.toStr() + " is already available";
		
		owner.free(this);
		this.isAvailable = true;
	}
	
	public String toStr() { return this.owner.toStr() + ":" + this.num; }
}
