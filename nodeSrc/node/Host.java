package node;

import common.Node;
import common.MACAddr;
import common.IPAddr;
import common.Port;
import common.Link;
import common.Packet;
import common.Header;

import java.util.Map;
import java.util.HashMap;

/// Basic Implementation of a Node
/// Hosts represent a computer/phone/etc
/// Hosts have exactly one port and so can connect to exactly one other node at a time
public class Host implements Node{
	private final String nickname;
	private final MACAddr mac = new MACAddr();
	private IPAddr ip;
	private Map<Port, Link> ports = new HashMap<>();

	/// @param name nickname for the given host, used internally
	/// {@return new host with auto-generated MAC address}
	/// @throws AssertionError
	public Host(String name) {
		assert !name.isBlank();
		this.nickname = name;
		
		ports.put(new Port(0, this), null);
	}

	/// Managed by Link object to update internal mappings of ports to nodes
	/// @param port Port to link
	/// @param link Link joining this Host to another
	/// @throws IllegalArgumentException
	/// @throws AssertionError
	public void link(Port port, Link link) {
		if (port == null) { throw new IllegalArgumentException("Specified port was null"); }
		if (link == null) { throw new IllegalArgumentException("Link was null"); }
		if (!ports.keySet().contains(port)) { throw new IllegalArgumentException("Port does not exist"); }
		assert ports.get(port) == null : "Specified port is unavailable";
		assert port.isAvailable() : "Specified port was unavailable";
		
		ports.put(port, link);
	}
	
	/// Managed by Link object to update internal mapping that link has been closed
	/// @param port Port to disconnect link
	/// @throws IllegalArgumentException
	/// @throws AssertionError
	public void free(Port port) {
		if (port == null) { throw new IllegalArgumentException("Specified port was null"); }
		if (!ports.keySet().contains(port)) { throw new IllegalArgumentException("Port does not exist"); }
		assert ports.get(port) != null : "Port was already free";
		
		ports.put(port, null);
	}
	
	/// Send a packet to host connected to port
	/// @param packet Packet to send
	/// @param port Port to send packet on
	/// @throws AssertionError
	public void sendPacket(Packet packet, Port port) {
		assert port != null;
		assert packet != null;
		
		Link l = ports.get(port);
		l.passPacket(packet, port);
	}
	
	/// Receives packet sent from another host in the network
	/// Automatically sends ACK packet when received
	/// @param packet Packet received from link
	/// @param port Port that received the packet
	/// @throws AssertionError
	public void acceptPacket(Packet packet, Port port) {
		assert port != null;
		assert packet != null;
		
		// Ensure received packet is not an ACK - avoids StackOverflow
		if (!packet.getClass().equals(ACKPacket.class)) {
			sendAck(packet.head(), port);
		}
	}
	
	/// Sends ACK packet directly back to origin
	/// @param head Header from original packet
	/// @param port Port to send ACK back through
	/// @throws AssertionError
	private void sendAck(Header head, Port port) {
		assert head != null;
		assert port != null;
		
		// Update header to return to sender
		Header ackHead = Header.of(head.dest(), head.src());
		Packet ACK = new ACKPacket(ackHead);
		sendPacket(ACK, port);
	}
	
	/// Finds next available port
	/// @return next open port on host
	/// @throws NullPointerException
	public Port getAvailablePort() {
		return ports.keySet().stream()
				.filter(p -> p.isAvailable())
				.findFirst()
				.orElseThrow(NullPointerException::new);
	}
	
	public MACAddr mac() { return this.mac; }
	public IPAddr ip() { return this.ip; }
	
	public String toStr() { return this.nickname; }
}
