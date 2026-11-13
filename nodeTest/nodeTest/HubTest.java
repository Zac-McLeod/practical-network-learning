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
		
		assertDoesNotThrow(() -> new Link(p1, p2));
	}
	
	@Test
	void linkMultipleHost() {
		Hub hub = new Hub("hub", 6);
		Host h1 = new Host("h1");
		Host h2 = new Host("h2");
		
		assertNotNull(hub);
		assertNotNull(h1);
		assertNotNull(h2);
	
		Port hub1 = hub.getAvailablePort();
		Port p1 = h1.getAvailablePort();
		Port p2 = h2.getAvailablePort();
		
		assertDoesNotThrow(() -> new Link(hub1, p1));
		
		Port hub2 = hub.getAvailablePort();
		assertNotEquals(hub1, hub2);
		assertDoesNotThrow(() -> new Link(hub2, p2));
	}
	
	@Test
	void forwardingTest() {
		Hub hub = new Hub("Hub Test hub", 6);
		Host h1 = new Host("Hub Test h1");
		Host h2 = new Host("Hub Test h2");
		Host h3 = new Host("Hub Test h3");
		
		assertNotNull(hub);
		assertNotNull(h1);
		assertNotNull(h2);
		assertNotNull(h3);
	
		Port hub1 = hub.getAvailablePort();
		Port p1 = h1.getAvailablePort();
		Port p2 = h2.getAvailablePort();
		Port p3 = h3.getAvailablePort();
		
		assertDoesNotThrow(() -> new Link(hub1, p1));
		
		Port hub2 = hub.getAvailablePort();
		assertDoesNotThrow(() -> new Link(hub2, p2));
		
		Port hub3 = hub.getAvailablePort();
		assertDoesNotThrow(() -> new Link(hub3, p3));
		
		String body = "Test";
		Header head = Header.of(h1.mac(), h3.mac());
		Packet p = new Packet(head, body.getBytes());
		
		assertDoesNotThrow(() -> h1.sendPacket(p, p1));
	}
}
