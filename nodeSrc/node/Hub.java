package node;

import common.Node;
import common.MACAddr;
import common.IPAddr;
import common.Port;
import common.Link;
import common.Packet;
import common.Header;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Hub implements Node{
	private final String nickname;
	private final MACAddr mac = new MACAddr();
	private IPAddr ip;
	private Map<Port, Link> ports = new HashMap<>();
	
	public Hub(String name, int numPorts) {
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
		ports.keySet().stream()
		.filter(e -> !e.isAvailable() && e != port)
		.forEach(e -> {
			this.sendPacket(packet, e);
		});
	}
	
	public MACAddr mac() { return this.mac; }
	public IPAddr ip() { return this.ip; }
	
	public String toStr() { return this.nickname; }
}
