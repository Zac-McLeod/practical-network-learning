package nodeTest;

import org.junit.jupiter.api.Test;

import node.Host;
import common.*;

import static org.junit.jupiter.api.Assertions.*;

class HostTest {
	
	@Test
	void creationValid() {
		assertDoesNotThrow(() -> new Host("Valid1"));
	}
	
	@Test
	void linkOneHost() {
		Host h1 = new Host("h1");
		Host h2 = new Host("h2");
		
		assertNotNull(h1);
		assertNotNull(h2);
	
		Port p1 = h1.getAvailablePort();
		Port p2 = h2.getAvailablePort();
		
		assertDoesNotThrow(() -> new Link(p1, p2, Protocol.ETH));
	}
	
	@Test
	void linkToUnavailablePort() {
		Host h1 = new Host("h1");
		Host h2 = new Host("h2");
		Host h3 = new Host("h3");
		
		assertNotNull(h1);
		assertNotNull(h2);
		assertNotNull(h3);
	
		Port p1 = h1.getAvailablePort();
		Port p2 = h2.getAvailablePort();
		Port p3 = h3.getAvailablePort();
		
		Link l1 = new Link(p1, p2, Protocol.ETH);
		assertThrows(AssertionError.class, () -> new Link(p1, p3, Protocol.ETH));
	}
	
	@Test
	void freePort() {
		Host h1 = new Host("h1");
		Host h2 = new Host("h2");
		
		assertNotNull(h1);
		assertNotNull(h2);
	
		Port p1 = h1.getAvailablePort();
		Port p2 = h2.getAvailablePort();
		
		Link l1 = new Link(p1, p2, Protocol.ETH);
		
		assertDoesNotThrow(() -> l1.remove());
		assertEquals(p1, h1.getAvailablePort());
		assertEquals(p2, h2.getAvailablePort());		
	}
}
