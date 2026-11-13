package node;

import common.Header;
import common.Packet;

/// Specialised implementation of Packet to simplify ACKs
public class ACKPacket extends Packet{
	public ACKPacket(Header head) {
		String ack = "ACK";
		byte[] body = ack.getBytes();
		super(head, body);
	}
}
