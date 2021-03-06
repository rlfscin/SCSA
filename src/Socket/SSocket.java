package Socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

import tool.AsymmetricCrypto;

public class SSocket {
	private Socket socket;
	private Peer peer;
	private static AsymmetricCrypto asyCrypto;
	private static String serverAddress;
	private static int serverPort;
	private static PublicKey serverPublicKey;
	private SSocketComunicator sscoketComunicator;

	private SSocket(String serverAddress, int serverPort) {
		try {
			asyCrypto = new AsymmetricCrypto();
			socket = null;
			SSocket.serverAddress = serverAddress;
			SSocket.serverPort = serverPort;

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

	private PublicKey logon() throws Exception{		
		Socket socket = new Socket(serverAddress, serverPort);
		// socket opened inside the authenticator
		SSocketAuthenticator ssAuthenticator = new SSocketAuthenticator(asyCrypto, socket);
		PublicKey serverkey = ssAuthenticator.authenticate();
		System.out.println("INFO: Authenticated to " + serverAddress +":"+serverPort); // TEST MESSAGE, REMOVE LATER!!!
		disconnect();
		return serverkey;
	}

	public void listen(int port) throws Exception{
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(port);
		System.out.println("INFO: waiting connection...");
		socket = server.accept();
		System.out.println("INFO: connection accepted from: " + server.getInetAddress().getHostAddress());
		sscoketComunicator = new SSocketComunicator(asyCrypto, socket);		
		sscoketComunicator.readTicket();		
	}

	public void connect(String targetIp, int port) throws Exception{
		socket = new Socket(serverAddress, serverPort);
		System.out.println("INFO: requesting connection to ip: " + targetIp + ":" + port); // TEST MESSAGE, REMOVE LATER!!!
		usePeer(targetIp);

		Socket socket = new Socket(peer.getAddress(), port);
		sscoketComunicator = new SSocketComunicator(asyCrypto, socket);
		sscoketComunicator.sendTicket(peer);
	}

	public void disconnect() {
		try {
			if (socket != null) socket.close();					
		} catch (Exception e) {
			// ignore
		}
	}


	public void sendFile(String filename) throws Exception{
		File file = new File(filename);
		long sizeFile = file.length();
		sendString(filename);
		@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[(int) sizeFile];
		fis.read(b, 0, b.length);
		sendObject(b);

	}
	public File receiveFile() throws IOException, Exception{
		String fileName = readString();
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] b = (byte[]) readObject();
		fos.write(b);
		fos.close();
		return file;
	}

	public void send(Serializable obj) throws Exception{
		System.out.println("INFO: Sending object");
		sscoketComunicator.sendObject(obj);
	}


	public Serializable receive() throws IOException, Exception{
		System.out.println("INFO: Receiving object");
		return sscoketComunicator.receiveObject();
	}

	public String readString() throws IOException, Exception{
		return (String) receive();
	}

	public int readInt() throws IOException, Exception{
		return (int) receive();
	}

	public long readlong() throws IOException, Exception{
		return (long) receive();
	}

	public double readDouble() throws IOException, Exception{
		return (double) receive();
	}

	public Serializable readObject() throws IOException, Exception{
		return receive();
	}

	public void sendString(String data) throws Exception{
		send(data);
	}

	public void sendInt(int data) throws Exception{
		send(data);
	}

	public void sendLong(long data) throws Exception{
		send(data);
	}

	public void sendDouble(double data) throws Exception{
		send(data);
	}

	public void sendObject(Serializable data) throws Exception{
		send(data);
	}


	private void usePeer(String targetAddress) throws Exception{		
		if (peer == null){			
			SSocketAuthenticator sSocketAuthenticator = new SSocketAuthenticator(asyCrypto, socket);
			peer = sSocketAuthenticator.requestSession(targetAddress, serverPublicKey); 						
		} 		
		socket.close();		
	}
}
