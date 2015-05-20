package Socket;

import java.net.InetAddress;

import javax.crypto.SecretKey;

public class Peer {
	
	public Peer(String address, SecretKey sessionKey) throws Exception{
		this.sessionKey = sessionKey;
		setAddress(address);
	}
	
	private InetAddress address;
	public SecretKey sessionKey;
	
	public String getAddress() {
		return address.toString();
	}
	public void setAddress(String address) throws Exception {
		this.address = InetAddress.getByName(address);
	} 
	
}
