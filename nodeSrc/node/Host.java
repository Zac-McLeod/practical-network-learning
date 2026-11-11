package node;

import common.Node;
import common.MACAddr;
import common.IPAddr;
import common.Port;
import common.Link;

import java.util.Map;
import java.util.HashMap;

public class Host implements Node{
	private final String nickname;
	private final MACAddr mac = new MACAddr();
	private IPAddr ip;
	private Map<Port, Link> ports = new HashMap<>();
	
	public Host(String name) {
		assert !name.isBlank();
		this.nickname = name;
		
		ports.put(new Port(0, this), null);
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
				.findFirst()
				.orElseThrow(NullPointerException::new);
	}
	
	public MACAddr mac() { return this.mac(); }
	public IPAddr ip() { return this.ip(); }
	
	public String toStr() { return this.nickname; }
}
