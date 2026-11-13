package node;

import common.Header;
import common.Packet;

public class ACKPacket extends Packet{
	public ACKPacket(Header head) {
		String ack = "ACK";
		byte[] body = ack.getBytes();
		super(head, body);
	}
}
