package Socket;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

import tool.AsymmetricCrypto;
import tool.SymmetricCrypto;

public class SSocket {
	private Socket socket;
	private Peer peer; // class that will store a single peer = (address + session Key)

	private static AsymmetricCrypto asyCrypto;

	//Server
	private static String serverAddress;
	private static int serverPort;
	private static PublicKey serverPublicKey;


	//Client
	private boolean connected;
	private String clientAddress;
	private int clientPort;
	private SSocketComunicator sscoketComunicator;

	private SSocket(String serverAddress, int serverPort) {
		try {
			asyCrypto = new AsymmetricCrypto();
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
	
	private SSocket() {
		try {
			socket = null;
			this.clientAddress = null;
			this.sscoketComunicator = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static SSocket getNewSSocket(String serverAddress, int serverPort){
		if(serverPublicKey != null){
			return new SSocket();
		}
		else{
			return new SSocket(serverAddress, serverPort);
		}
		
	}
	
	public void listen(int port) throws Exception{
		ServerSocket server = new ServerSocket(port);
		System.out.println("CLIENT: waiting connection..."); // TEST MESSAGE, REMOVE LATER!!!
		socket = server.accept();
		System.out.println("CLIENT: connection accepted."); // TEST MESSAGE, REMOVE LATER!!!
		sscoketComunicator = new SSocketComunicator(asyCrypto, socket);
		sscoketComunicator.readTicket();
		System.out.println("CLIENT: Ticket ok."); // TEST MESSAGE, REMOVE LATER!!!
		
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

	public void connect(String targetIp, int port) throws Exception{
		socket = new Socket(serverAddress, serverPort);
		System.out.println("CLIENT: CONNECT: requesting connection to ip: " + targetIp + ":" + port); // TEST MESSAGE, REMOVE LATER!!!
		usePeer(targetIp);

		//okay but commented out for testing
		System.out.println(peer.getAddress()+ ":" + port);
		Socket socket = new Socket(peer.getAddress(), port);
		sscoketComunicator = new SSocketComunicator(asyCrypto, socket);
		sscoketComunicator.sendTicket(peer.ticket);
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
	
	public String readString() throws IOException, Exception{
		return (String) receive();
	}
	
	
	
	
	public void sendString(String data) throws Exception{
		send(data);
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
		socket.close();		
	}
}
