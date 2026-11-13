package common;

public record Header(MACAddr src, MACAddr dest) {
	public String toStr() {
		return src.toStr() +" -> "+dest.toStr();
	}
}
