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
	private Peer peer; // class that will store a single peer = (address + session Key)

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
		socket = new Socket(serverAddress, serverPort);
		System.out.println("CLIENT: CONNECT: requesting connection to ip: " + ip); // TEST MESSAGE, REMOVE LATER!!!
		usePeer(ip);

		//okay but commented out for testing
		//Socket socket = new Socket(peer.getAddress(), port);
		//sscoketComunicator = new SSocketComunicator(symCrypto, socket, peer);
	}

	public void disconnect() {
		try {
			if (socket != null) socket.close();					
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

	private void usePeer(String targetAddress) throws Exception{		
		if (peer == null){			
			SSocketAuthenticator sSocketAuthenticator = new SSocketAuthenticator(asyCrypto, socket);
			peer = sSocketAuthenticator.requestSession(targetAddress, serverPublicKey); 						
		} 		
		socket=null;		
	}
}
