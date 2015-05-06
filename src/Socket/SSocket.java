package Socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import tool.AsymmetricCrypto;
import tool.SymmetricCrypto;

public class SSocket {
	private AsymmetricCrypto asyCrypto;
	private SymmetricCrypto symCrypto;
	private Socket socket;
	private ArrayList<Peer> peers; // array of the class that will store a single peer = (address + session Key)
	private String serverAddress;
	private int serverPort;
	private byte[] serverPublicKey;
	
	public SSocket(String serverAddress, int serverPort) throws Exception{
		try {
			asyCrypto = new AsymmetricCrypto();
			symCrypto = new SymmetricCrypto();
			socket = null;
			this.serverAddress = serverAddress;
			this.serverPort = serverPort;
			
			serverPublicKey = logon();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private byte[] logon() throws UnknownHostException, IOException{
		this.socket = new Socket(InetAddress.getByName(serverAddress), serverPort);
		// socket opened inside the authenticator
		SSocketAuthenticator ssAuthenticator = new SSocketAuthenticator(asyCrypto, socket);
		byte[] serverkey = ssAuthenticator.authenticate();
		socket.close();
		return serverkey;
	}
	
	private void communicate(String address){
		//this.socket = new Socket("localhost", 5999);
		//SSocketComunicator sscommunicator = new SSocketComunicator(symCrypto, socket, peers.get(indexOfPeer(address)));
	}
	
	private void addPeer(String address, byte[] sessionKey) throws Exception{
		int index = -1;
		if ((index = indexOfPeer(address)) >= 0)
			return;
		
		peers.add(new Peer(address, sessionKey));
	}

	private int indexOfPeer(String address){
		boolean isThere = false;
			for (Peer peer : peers) {
				return peers.indexOf(peer);				
			}
			return -1;
	}
	
	private void removePeer(String address){
		int index = -1;
		if ((index = indexOfPeer(address)) >= 0)
			return;
		
		peers.remove(index);
	}
	
	private boolean validatePeer(){
		// CODE
		// used to validate if peer gotten from server is up.
		return false;
	}
}
