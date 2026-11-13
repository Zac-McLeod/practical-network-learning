package node;

import common.Node;
import common.MACAddr;
import common.Header;
import common.IPAddr;
import common.Port;
import common.Link;
import common.Packet;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/// Implementation of an L1 Hub
/// Hubs will flood their collision domain with any received packet
/// Hubs can have a user determined number of Nodes attached
/// Hubs can be connected with other hubs, switches, or hosts
public class Hub implements Node{
	private final String nickname;
	private final MACAddr mac = new MACAddr();
	private IPAddr ip;
	private Map<Port, Link> ports = new HashMap<>();
	
	/// @param name nickname for the hub, used internally
	/// @param numPorts Number of available connections
	/// {@return new hub with auto-generated MAC address}
	/// @throws AssertionError
	public Hub(String name, int numPorts) {
		assert !name.isBlank();
		assert numPorts > 0;
		
		this.nickname = name;
		
		// Automatically fill ports map with null links
		IntStream.range(0,numPorts).forEach(i -> {
			ports.put(new Port(i, this), null);
		});
	}
	
	/// Managed by Link object to update internal mappings of ports to nodes
	/// @param port Port that link will connect to
	/// @param link Link joining this Node to another
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
	public void free(Port p) {
		if (p == null) { throw new IllegalArgumentException("Specified port was null"); }
		if (!ports.keySet().contains(p)) { throw new IllegalArgumentException("Port does not exist"); }
		assert ports.get(p) != null : "Port was already free";
		
		ports.put(p, null);
	}
	
	/// Forward a packet to node connected to port
	/// @param packet Packet to forward
	/// @param port Port to forward packet on
	/// @throws AssertionError
	public void sendPacket(Packet packet, Port port) {
		Link l = ports.get(port);
		l.passPacket(packet, port);
	}
	
	/// Flood collision domain with any received packet
	/// @param packet Packet to flood frame with
	/// @param port Port that received packet
	/// @throws AssertionError
	public void acceptPacket(Packet packet, Port port) {
		assert packet != null;
		assert port != null;
		
		MACAddr sender = packet.head().src();
		assert sender != null;
		
		MACAddr recipient = packet.head().dest();
		assert recipient != null;
		
		int ttl = packet.head().ttl();
		assert ttl > 0; // TODO: handle TTL hitting 0 - implement ICMP for Dead packets
		
		Header newHead = Header.of(sender, recipient, ttl-1);
		Packet repacked = new Packet(newHead, packet.body());
		
		// Forward packet to all ports excluding incoming port
		ports.keySet().stream()
		.filter(e -> !e.isAvailable() && e != port)
		.forEach(e -> {
			this.sendPacket(repacked, e);
		});
	}
	
	/// Finds next available port
	/// @return next open port on host
	/// @throws NullPointerException
	public Port getAvailablePort() {
		return ports.keySet().stream()
				.filter(p -> p.isAvailable())
				// Ports become unavailable sequentially
				.sorted(Comparator.comparing(Port::portNum))
				.findFirst()
				.orElseThrow(NullPointerException::new);
	}
	
	
	public MACAddr mac() { return this.mac; }
	public IPAddr ip() { return this.ip; }
	
	public String toStr() { return this.nickname; }
}
