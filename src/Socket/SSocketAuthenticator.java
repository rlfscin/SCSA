package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;

import tool.AsymmetricCrypto;

public class SSocketAuthenticator {
	private Socket socket;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	private AsymmetricCrypto asyCrypto;
	private String serverAddress;
	private int port;
	
	public SSocketAuthenticator(AsymmetricCrypto asyCrypto, Socket socket){
		try {
			this.dataInput = new DataInputStream(this.socket.getInputStream());
			this.dataOutput = new DataOutputStream(this.socket.getOutputStream());
			this.asyCrypto = asyCrypto;
			this.socket = socket;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PublicKey authenticate() throws UnknownHostException, IOException {
		PublicKey serverPublicKey = null;
		
		//socket = new Socket(InetAddress.getByName(serverAddress), port ,false);
		
		// socket stuff
		dataInput = new DataInputStream(socket.getInputStream());
		dataOutput = new DataOutputStream(socket.getOutputStream());
		
		
		// serverPublicKey = ask server's public
		// send my public
		// xoxo

		socket.close();

		// returns server's public to SSocket for later usage.
		//return serverPublicKey.getEncoded();
		return null;
	}
	
	public byte[] requestSession(String address) throws UnknownHostException, IOException{
		
		// socket stuff
		dataInput = new DataInputStream(socket.getInputStream());
		dataOutput = new DataOutputStream(socket.getOutputStream());
		
		// send server a request of an address
		// get key (byte[])
		// xoxo

		socket.close();
		
		//return the key
		return null;
	}
}
