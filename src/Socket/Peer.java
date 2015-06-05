package Socket;

import javax.crypto.SecretKey;

public class Peer {

	public Peer(String address, SecretKey sessionKey, byte[] ticket) throws Exception{
		this.sessionKey = sessionKey;
		setAddress(address);
		this.ticket = ticket;
	}

	private String address;
	public SecretKey sessionKey;
	public byte[] ticket;

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) throws Exception {
		this.address = address;
	} 

}
