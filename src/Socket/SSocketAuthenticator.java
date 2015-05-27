package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import tool.AsymmetricCrypto;
import tool.Basket;
import tool.Header;
import tool.Parser;

public class SSocketAuthenticator {
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private AsymmetricCrypto asyCrypto;
	//private String serverAddress;
	//private int port;
	
	public SSocketAuthenticator(AsymmetricCrypto asyCrypto, Socket socket){
		try {
			this.socket = socket;
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			this.outputStream = new DataOutputStream(this.socket.getOutputStream());
			this.asyCrypto = asyCrypto;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PublicKey authenticate() throws Exception {
		//requesting service
		Basket basketRequest = new Basket(Header.GetPublicKey, null);
		flush(Parser.parseByte(basketRequest));
		
		System.out.println("CLIENT: requesting public key"); // TEST MESSAGE, REMOVE LATER!!!
		
		// reading response
		Basket basketResponse = (Basket) Parser.parseObject(read());		
		PublicKey serverPublicKey = (PublicKey) Parser.parseObject(basketResponse.getData());
		
		if (basketResponse.getHeader() == Header.SendPublicKey) 
			System.out.println("Server's up. Authenticating." + System.lineSeparator()); // consider revising here later
		
		//sending my key!
		Basket basketSendKey = new Basket(Header.SendPublicKey, Parser.parseByte(asyCrypto.getPublicKey()));
		
		System.out.println("CLIENT: this is my request size: " + Parser.parseByte(basketSendKey).length); // TEST MESSAGE, REMOVE LATER!!!
		byte[] cipherBasketSendKey = asyCrypto.encrypt(Parser.parseByte(basketSendKey), serverPublicKey);
		System.out.println("CLIENT: this is the server pub. key: " + serverPublicKey.hashCode()); // TEST MESSAGE, REMOVE LATER!!!
		System.out.println("CLIENT: this is my ENCRYPTED request size: " + cipherBasketSendKey.length); // TEST MESSAGE, REMOVE LATER!!!
		flush(cipherBasketSendKey);
		
		// test message 
		System.out.println("CLIENT: my public key was sent!: " + Parser.parseByte(asyCrypto.getPublicKey())); // TEST MESSAGE, REMOVE LATER!!!
		
		//returning the key I got
		return serverPublicKey;
		//authenticated.
	}
	
	public SecretKey requestSession(String address) throws UnknownHostException, IOException{
		
		socket.close();
		
		//return the key
		return null;
	}
	private void flush(byte[] bytes) throws IOException{
		//TODO send the size of the basket
		outputStream.writeInt(bytes.length);
		outputStream.write(bytes);
		//socket.shutdownOutput();	
	}

	//do NOT used directly! no cryptography implemented
	private byte[] read() {
		//TODO receive the size of the basket
		byte[] bytes = null;
		try {
			int length = inputStream.readInt();
			bytes = new byte[length];
			inputStream.read(bytes, 0, length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
	
}
