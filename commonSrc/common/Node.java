package common;

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