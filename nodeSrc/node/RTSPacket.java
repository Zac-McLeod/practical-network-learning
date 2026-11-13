package node;

import common.Header;
import common.Packet;

public class RTSPacket extends Packet{
	public RTSPacket(Header head) {
		String rts = "rts";
		byte[] body = rts.getBytes();
		super(head, body);
	}
}
