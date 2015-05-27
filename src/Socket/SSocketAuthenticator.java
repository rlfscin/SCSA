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
		
		if (basketResponse.getHeader() == Header.SendPublicKey) 
			System.out.println("Server's up. Authenticating." + System.lineSeparator()); // consider revising here later
		
		//sending my key!
		Basket basketSendKey = new Basket(Header.SendPublicKey, Parser.parseByte(asyCrypto.getPublicKey()));
		byte[] cipherBasketSendKey = asyCrypto.encrypt(Parser.parseByte(basketSendKey));
		flush(cipherBasketSendKey);
		
		// test message 
		System.out.println("CLIENT: my public key was sent!: " + Parser.parseByte(asyCrypto.getPublicKey())); // TEST MESSAGE, REMOVE LATER!!!
		
		//returning the key I got
		return (PublicKey) Parser.parseObject(basketResponse.getData());
		//authenticated.
	}
	
	public SecretKey requestSession(String address) throws UnknownHostException, IOException{
		
		socket.close();
		
		//return the key
		return null;
	}
	private void flush(byte[] bytes) throws IOException{
		//TODO send the size of the basket
		outputStream.write(bytes);
		//socket.shutdownOutput();	
	}

	//do NOT used directly! no cryptography implemented
	private byte[] read() throws IOException{
		//TODO receive the size of the basket
		byte[] bytes = new byte[2048];		
		inputStream.read(bytes);
		System.out.println("read."); // TEST MESSAGE, REMOVE LATER!!!
		return bytes;
	}
	
}
