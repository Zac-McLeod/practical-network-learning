package nodeTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import node.Host;
import node.Switch;
import common.*;

class SwitchTest {

	@Test
	void creationValid() {
		int numPorts = 6;
		assertDoesNotThrow(() -> new Switch("Valid1", numPorts));
	}
	
	@Test
	void linkOneHost() {
		Switch hub = new Switch("switch", 6);
		Host h1 = new Host("h2");
		
		assertNotNull(hub);
		assertNotNull(h1);
	
		Port p1 = hub.getAvailablePort();
		Port p2 = h1.getAvailablePort();
		
		assertDoesNotThrow(() -> new Link(p1, p2));
	}
	
	@Test
	void linkMultipleHost() {
		Switch hub = new Switch("switch", 6);
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
	void floodFrameTest() {
		Switch s = new Switch("Switch Test switch", 6);
		Host h1 = new Host("Switch Test h1");
		Host h2 = new Host("Switch Test h2");
		Host h3 = new Host("Switch Test h3");
		
		assertNotNull(s);
		assertNotNull(h1);
		assertNotNull(h2);
		assertNotNull(h3);
	
		Port s1 = s.getAvailablePort();
		Port p1 = h1.getAvailablePort();
		Port p2 = h2.getAvailablePort();
		Port p3 = h3.getAvailablePort();
		
		assertDoesNotThrow(() -> new Link(s1, p1));
		
		Port s2 = s.getAvailablePort();
		assertDoesNotThrow(() -> new Link(s2, p2));
		
		Port s3 = s.getAvailablePort();
		assertDoesNotThrow(() -> new Link(s3, p3));
		
		String body = "Test";
		Header head = Header.of(h1.mac(), h3.mac());
		Packet p = new Packet(head, body.getBytes());
		
		assertDoesNotThrow(() -> h1.sendPacket(p, p1));
	}
	@Test
	void switchingTest() {
		Switch s = new Switch("Switch Test switch", 6);
		Host h1 = new Host("Switch Test h1");
		Host h2 = new Host("Switch Test h2");
		Host h3 = new Host("Switch Test h3");
		
		assertNotNull(s);
		assertNotNull(h1);
		assertNotNull(h2);
		assertNotNull(h3);
	
		Port s1 = s.getAvailablePort();
		Port p1 = h1.getAvailablePort();
		Port p2 = h2.getAvailablePort();
		Port p3 = h3.getAvailablePort();
		
		assertDoesNotThrow(() -> new Link(s1, p1));
		
		Port s2 = s.getAvailablePort();
		assertDoesNotThrow(() -> new Link(s2, p2));
		
		Port s3 = s.getAvailablePort();
		assertDoesNotThrow(() -> new Link(s3, p3));
		
		String body = "Test";
		Header head = Header.of(h1.mac(), h3.mac());
		Packet packet1 = new Packet(head, body.getBytes());
		
		assertDoesNotThrow(() -> h1.sendPacket(packet1, p1));
		
		String body2 = "bodyTest2";
		Header head2 = Header.of(h1.mac(), h3.mac());
		Packet packet2 = new Packet(head2, body2.getBytes());
		assertDoesNotThrow(() -> h1.sendPacket(packet2, p1));
	}
}
