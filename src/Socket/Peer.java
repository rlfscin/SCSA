package Socket;

import java.net.InetAddress;

public class Peer {
	private InetAddress address;
	public byte[] sessionKey;
	
	public String getAddress() {
		return address.toString();
	}
	public void setAddress(String address) throws Exception {
		this.address = InetAddress.getByName(address);
	} 
	
}
