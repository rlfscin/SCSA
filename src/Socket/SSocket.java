package Socket;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;
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
	private PublicKey serverPublicKey;

	public SSocket(String serverAddress, int serverPort) {
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
	
	private PublicKey logon() throws UnknownHostException, IOException{
		this.socket = new Socket(serverAddress, serverPort);
		// socket opened inside the authenticator
		SSocketAuthenticator ssAuthenticator = new SSocketAuthenticator(asyCrypto, socket);
		PublicKey serverkey = ssAuthenticator.authenticate();
		socket.close();
		return serverkey;
	}

	public void sendFile(String filename, String address) throws Exception{
		Peer peer = use(address);

		SSocketComunicator sscommunicator = new SSocketComunicator(symCrypto, socket, peer);
		//sscommunicator.sendFile(filename);
	}

	public void receiveFile(){
		// TODO: IMPLEMENT!
	}
	
	public void sendText(String message, String address) throws Exception{
		Peer peer = use(address);

		SSocketComunicator sscommunicator = new SSocketComunicator(symCrypto, socket, peer);
		sscommunicator.sendText(message);
	}	
	
	public void receiveText(){
		//TODO:  IMPLEMENT!
	}
	
	public void sendObject(Serializable object, String address) throws Exception{
		Peer peer = use(address);

		SSocketComunicator sscommunicator = new SSocketComunicator(symCrypto, socket, peer);
		sscommunicator.sendObject(object);
	}	
	
	public void receiveObject(){
		//TODO:  IMPLEMENT!
	}

	

	

	private Peer use(String address) throws Exception{
		int index = -1;
		index = indexOfPeer(address);
		if (index == -1){
			SSocketAuthenticator sSocketAuthenticator = new SSocketAuthenticator(asyCrypto, socket);
			byte[] sessionKey = sSocketAuthenticator.requestSession(address); 
			addPeer(address, sessionKey);
		}
		index = indexOfPeer(address);

		validatePeer(peers.get(index));

		return peers.get(index);
	}

	private void addPeer(String address, byte[] sessionKey) throws Exception{
		int index = -1;
		if ((index = indexOfPeer(address)) >= 0)
			peers.set(index, (new Peer(address, sessionKey)));
		else peers.add(new Peer(address, sessionKey));
	}

	private int indexOfPeer(String address){
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

	private boolean validatePeer(Peer peer){
		// CODE
		// used to validate if peer gotten from server is up.
		return false;
	}
	
}
