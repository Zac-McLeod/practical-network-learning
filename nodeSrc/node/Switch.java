package node;

import common.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.List;
import java.util.ArrayList;

/// Representation of a switches Forwarding Table entry 
/// @param mac MAC address of sender node
/// @param port Port node is on
/// @param ttl Number of hops sender node is from this node
record FTEntry(MACAddr mac, Port port, int ttl) {}

/// Container for FTEntries
final class ForwardingTable{
	private List<FTEntry> entries = new ArrayList<>();
	public ForwardingTable() {}
	
	/// @param entry FTEntry to check
	/// {@return whether the switches forwarding table contains the given entry}
	public boolean contains(FTEntry entry) { return entries.contains(entry); }
	
	/// @param mac MAC address to find in FT
	/// {@return whether the switches forwarding table contains an entry with given MAC}
	/// @throws AssertionError
	public boolean contains(MACAddr mac) {
		assert mac != null;
		List<FTEntry> matches = entries.stream()
			.filter(e-> e.mac() == mac)
			.toList();
		return matches.size() == 1;
	}
	
	/// Get entry by MAC address
	/// @param mac MAC address to find
	/// {@return FTEntry with MAC address}
	/// @throws AssertionError
	public FTEntry get(MACAddr mac) {
		assert mac != null;
		List<FTEntry> matches = entries.stream()
			.filter(e-> e.mac() == mac)
			.toList();
		assert matches.size() == 1;
		return matches.get(0);
	}
	
	/// Get entry by Port
	/// @param port Port to find
	/// {@return FTEntry on port}
	/// @throws AssertionError
	public FTEntry get(Port port) {
		assert port != null;
		List<FTEntry> matches = entries.stream()
			.filter(e-> e.port() == port)
			.toList();
		assert matches.size() == 1;
		return matches.get(0);
	}
	
	/// Add FTEntry to table
	/// @param entry FTEntry to add
	public void add(FTEntry entry) { entries.add(entry); }
	
	public String toStr() { return entries.toString(); }
}

/// Implementation of an L2 Switch
/// Switches will forward a packet toward the destination node
/// If a switch does not know where the destination node is, it will act as a hub for that packet
/// Switches can have a user determined number of nodes attached
/// Switches can connect to Hosts, other Switches, or hubs
public class Switch implements Node{
	private final String nickname;
	private final MACAddr mac = new MACAddr();
	private IPAddr ip;
	private Map<Port, Link> ports = new HashMap<>();
	private ForwardingTable ft = new ForwardingTable();
	
	/// @param name nickname for the switch, used internally
	/// @param numPorts Number of available connections
	/// {@return new hub with auto-generated MAC address}
	/// @throws AssertionError
	public Switch(String name, int numPorts) {
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
	public void link(Port p, Link l) {
		if (p == null) { throw new IllegalArgumentException("Specified port was null"); }
		if (l == null) { throw new IllegalArgumentException("Link was null"); }
		if (!ports.keySet().contains(p)) { throw new IllegalArgumentException("Port does not exist"); }
		assert ports.get(p) == null : "Specified port is unavailable";
		assert p.isAvailable() : "Specified port was unavailable";
		
		ports.put(p, l);
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
	
	/// Forward packet to necessary port otherwise flood collision domain
	/// @param packet Packet to forward
	/// @param port Port of origin
	/// @throws AssertionError
	public void acceptPacket(Packet packet, Port port) {
		assert packet != null;
		assert port != null;
		
		MACAddr sender = packet.head().src();
		assert sender != null;
		
		MACAddr recipient = packet.head().dest();
		assert recipient != null;
		
		Port originPort = port;
		int ttl = packet.head().ttl();
		assert ttl > 0; // TODO: handle TTL hitting 0 - implement ICMP for Dead packets
		
		FTEntry entry = new FTEntry(sender, originPort, ttl);
		
		if (!ft.contains(entry)) { ft.add(entry); }
		
		Header newHead = Header.of(sender, recipient, ttl-1);
		Packet repacked = new Packet(newHead, packet.body());
		
		// Handle forwarding
		if (ft.contains(recipient)) {
			Port recipientPort = ft.get(recipient).port();
			if (originPort == recipientPort) { return; }
			sendPacket(repacked, recipientPort);
		} else {
			floodFrame(repacked, port);
		}
	}
	
	/// Flood collision domain with any received packet
	/// @param packet Packet to flood frame with
	/// @param port Port that received packet
	private void floodFrame(Packet packet, Port origin) {
		// Forward packet to all ports excluding incoming port
		ports.keySet().stream()
		.filter(e -> !e.isAvailable() && e != origin)
		.forEach(e -> {
			this.sendPacket(packet, e);
		});
	}
	
	/// Finds next available port
	/// @return next open port on host
	/// @throws NullPointerException
	public Port getAvailablePort() {
		return ports.keySet().stream()
				.filter(p -> p.isAvailable())
				.sorted(Comparator.comparing(Port::portNum))
				.findFirst()
				.orElseThrow(NullPointerException::new);
	}
	
	public MACAddr mac() { return this.mac; }
	public IPAddr ip() { return this.ip; }
	
	public String toStr() { return this.nickname; }
}