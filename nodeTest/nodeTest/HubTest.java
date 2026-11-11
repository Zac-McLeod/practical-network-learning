package nodeTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import node.Host;
import node.Hub;
import common.*;

class HubTest {

	@Test
	void creationValid() {
		int numPorts = 6;
		assertDoesNotThrow(() -> new Hub("Valid1", numPorts));
	}
	
	@Test
	void linkOneHost() {
		Hub hub = new Hub("hub", 6);
		Host h1 = new Host("h2");
		
		assertNotNull(hub);
		assertNotNull(h1);
	
		Port p1 = hub.getAvailablePort();
		Port p2 = h1.getAvailablePort();
		
		assertDoesNotThrow(() -> new Link(p1, p2, Protocol.ETH));
	}
	
	@Test
	void linkMultipleHost() {
		Hub hub = new Hub("hub1", 6);
		Host h1 = new Host("h1");
		Host h2 = new Host("h2");
		
		assertNotNull(hub);
		assertNotNull(h1);
		assertNotNull(h2);
	
		Port hub1 = hub.getAvailablePort();
		Port p1 = h1.getAvailablePort();
		Port p2 = h2.getAvailablePort();
		
		assertDoesNotThrow(() -> new Link(hub1, p1, Protocol.ETH));
		
		Port hub2 = hub.getAvailablePort();
		assertNotEquals(hub1, hub2);
		assertDoesNotThrow(() -> new Link(hub2, p2, Protocol.ETH));
	}
	
}
