package node;

import common.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.List;
import java.util.ArrayList;

record FTEntry(MACAddr mac, Port port, int ttl) {}

final class ForwardingTable{
	private List<FTEntry> entries = new ArrayList<>();
	public ForwardingTable() {}
	
	public boolean contains(FTEntry entry) { return entries.contains(entry); }
	public boolean contains(MACAddr mac) {
		assert mac != null;
		List<FTEntry> matches = entries.stream()
			.filter(e-> e.mac() == mac)
			.toList();
		return matches.size() == 1;
	}
	
	public FTEntry get(MACAddr mac) {
		assert mac != null;
		List<FTEntry> matches = entries.stream()
			.filter(e-> e.mac() == mac)
			.toList();
		assert matches.size() == 1;
		return matches.get(0);
	}
	public FTEntry get(Port port) {
		assert port != null;
		List<FTEntry> matches = entries.stream()
			.filter(e-> e.port() == port)
			.toList();
		assert matches.size() == 1;
		return matches.get(0);
	}
	public void add(FTEntry entry) { entries.add(entry); }
	public String toStr() { return entries.toString(); }
}


public class Switch implements Node{
	private final String nickname;
	private final MACAddr mac = new MACAddr();
	private IPAddr ip;
	private Map<Port, Link> ports = new HashMap<>();
	private ForwardingTable ft = new ForwardingTable();
	
	public Switch(String name, int numPorts) {
		assert !name.isBlank();
		assert numPorts > 0;
		
		this.nickname = name;
		
		IntStream.range(0,numPorts).forEach(i -> {
			ports.put(new Port(i, this), null);
		});
	}
	
	public void link(Port p, Link l) {
		if (p == null) { throw new IllegalArgumentException("Specified port was null"); }
		if (l == null) { throw new IllegalArgumentException("Link was null"); }
		if (!ports.keySet().contains(p)) { throw new IllegalArgumentException("Port does not exist"); }
		assert ports.get(p) == null : "Specified port is unavailable";
		assert p.isAvailable() : "Specified port was unavailable";
		
		ports.put(p, l);
	}
	
	public void free(Port p) {
		if (p == null) { throw new IllegalArgumentException("Specified port was null"); }
		if (!ports.keySet().contains(p)) { throw new IllegalArgumentException("Port does not exist"); }
		assert ports.get(p) != null : "Port was already free";
		
		ports.put(p, null);
	}
	
	public Port getAvailablePort() {
		return ports.keySet().stream()
				.filter(p -> p.isAvailable())
				.sorted(Comparator.comparing(Port::portNum))
				.findFirst()
				.orElseThrow(NullPointerException::new);
	}
	
	public void sendPacket(Packet packet, Port port) {
		Link l = ports.get(port);
		l.passPacket(packet, port);
	}
	
	public void acceptPacket(Packet packet, Port port) {
		MACAddr sender = packet.head().src();
		MACAddr recipient = packet.head().dest();
		Port originPort = port;
		int ttl = packet.head().ttl();
		
		FTEntry entry = new FTEntry(sender, originPort, ttl);
		
		if (!ft.contains(entry)) { ft.add(entry); }
		
		if (ft.contains(recipient)) {
			Port recipientPort = ft.get(recipient).port();
			if (originPort == recipientPort) { return; }
			sendPacket(packet, recipientPort);
		} else {
			floodFrame(packet, port);
		}
	}
	
	private void floodFrame(Packet packet, Port origin) {
		ports.keySet().stream()
		.filter(e -> !e.isAvailable() && e != origin)
		.forEach(e -> {
			this.sendPacket(packet, e);
		});
	}
	
	public MACAddr mac() { return this.mac; }
	public IPAddr ip() { return this.ip; }
	
	public String toStr() { return this.nickname; }
}