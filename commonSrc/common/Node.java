package common;

/// Nodes are any device in the network
/// Currently Nodes are implemented by Hosts, Hubs, Switches
/// Planning on DNS, DHCP, and Router implementations to also use this interface
public interface Node{
	public void link(Port p, Link l);
	public void free(Port p);
	
	public void sendPacket(Packet p, Port port);
	public void acceptPacket(Packet p, Port port);
	
	public Port getAvailablePort();
	public MACAddr mac();
	public IPAddr ip();
	
	public String toStr();
}