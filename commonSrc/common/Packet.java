package common;

public record Packet(Header head, byte[] body) {}