package Socket;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import tool.AsymmetricCrypto;
import tool.SymmetricCrypto;

public class SSocket {
	private Socket socket;
	private ArrayList<Peer> peers; // array of the class that will store a single peer = (address + session Key)

	private AsymmetricCrypto asyCrypto;

	//Server
	private String serverAddress;
	private int serverPort;
	private PublicKey serverPublicKey;


	//Client
	private boolean connected;
	private String clientAddress;
	private int clientPort;
	private SymmetricCrypto symCrypto;
	private SSocketComunicator sscoketComunicator;

	public SSocket(String serverAddress, int serverPort) {
		try {
			asyCrypto = new AsymmetricCrypto();
			symCrypto = new SymmetricCrypto();
			socket = null;
			this.serverAddress = serverAddress;
			this.serverPort = serverPort;

			this.clientAddress = null;
			this.sscoketComunicator = null;

			serverPublicKey = logon();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private PublicKey logon() throws Exception{
		System.out.println("CLIENT: logging on."); // TEST MESSAGE, REMOVE LATER!!!
		Socket socket = new Socket(serverAddress, serverPort);
		// socket opened inside the authenticator
		SSocketAuthenticator ssAuthenticator = new SSocketAuthenticator(asyCrypto, socket);
		PublicKey serverkey = ssAuthenticator.authenticate();
		System.out.println("CLIENT: got server's key: " + serverkey); // TEST MESSAGE, REMOVE LATER!!!
		disconnect();
		return serverkey;
	}

	public void connect(String ip, int port) throws Exception{
		System.out.println("CLIENT: CONNECT: connecting to ip: " + ip); // TEST MESSAGE, REMOVE LATER!!!
		Peer peer = usePeer(ip);

		//okay but commented out for testing
		//Socket socket = new Socket(peer.getAddress(), port);
		//sscoketComunicator = new SSocketComunicator(symCrypto, socket, peer);
	}

	public void disconnect() {
		try {
			socket.close();					
		} catch (Exception e) {
			// ignore
		}
	}


	public void sendFile(String filename, String address) throws Exception{
		//TODO fix
		/*

		SSocketComunicator sscommunicator = new SSocketComunicator(symCrypto, socket, peer);
		//sscommunicator.sendFile(filename);
		 */		 
	}

	public void send(Serializable obj) throws Exception{
		//TODO error connection
		sscoketComunicator.sendObject(obj);
	}

	public void receiveFile(){
		// TODO: IMPLEMENT!
	}

	public Serializable receive() throws IOException, Exception{
		//TODO error connection
		return sscoketComunicator.receiveObject();
	}


	/*
	 * 
	 * Danger zone
	 */
	/*
	public void sendText(String message, String address) throws Exception{
		Peer peer = use(address);

		SSocketComunicator sscommunicator = new SSocketComunicator(symCrypto, socket, peer);
		sscommunicator.sendText(message);
	}	

	 */
	public void receiveText(){
		//TODO:  IMPLEMENT!
	}

	public void sendObject(Serializable object, String address) throws Exception{
		/*
		SSocketComunicator sscommunicator = new SSocketComunicator(symCrypto, socket, peer);
		sscommunicator.sendObject(object);
		 */
	}	

	private Peer usePeer(String targetAddress) throws Exception{
		int index = -1;
		index = indexOfPeer(targetAddress);
		if (index == -1){			
			SSocketAuthenticator sSocketAuthenticator = new SSocketAuthenticator(asyCrypto, socket);
			Peer newPeer = sSocketAuthenticator.requestSession(targetAddress, serverPublicKey); 
			addPeer(newPeer);
			index = indexOfPeer(targetAddress);
		} 		

		socket=null;
		return peers.get(index);
	}

	private void addPeer(String address, SecretKey sessionKey, byte[] ticket) throws Exception{
		int index = -1;
		if ((index = indexOfPeer(address)) >= 0)
			peers.set(index, (new Peer(address, sessionKey, ticket)));
		else peers.add(new Peer(address, sessionKey, ticket));
	}
	
	private void addPeer(Peer peer) throws Exception{
		int index = -1;
		if ((index = indexOfPeer(peer.getAddress())) >= 0)
			peers.set(index, peer);
		else peers.add(peer);
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
}
