package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.Arrays;

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
	private SecretKey sessionKey;		
	private byte[] ticket;
	
	private int TICKETLENGTH = 256;
	private int SESSIONLENGTH = 152;
	
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
		
		//System.out.println("CLIENT: requesting public key"); // TEST MESSAGE, REMOVE LATER!!!
		
		// reading response
		Basket basketResponse = (Basket) Parser.parseObject(read());		
		PublicKey serverPublicKey = (PublicKey) Parser.parseObject(basketResponse.getData());
		
		if (basketResponse.getHeader() == Header.SendPublicKey) 
			System.out.println("Server's up. Authenticating." + System.lineSeparator()); // consider revising here later
		
		//sending my key!
		Basket basketSendKey = new Basket(Header.SendPublicKey, Parser.parseByte(asyCrypto.getPublicKey()));
		
		//System.out.println("CLIENT: this is my request size: " + Parser.parseByte(basketSendKey).length); // TEST MESSAGE, REMOVE LATER!!!
		byte[] cipherBasketSendKey = asyCrypto.encrypt(Parser.parseByte(basketSendKey), serverPublicKey);
		//System.out.println("CLIENT: this is the server pub. key: " + serverPublicKey); // TEST MESSAGE, REMOVE LATER!!!
		//System.out.println("CLIENT: this is the server pub. key HASH: " + serverPublicKey.hashCode()); // TEST MESSAGE, REMOVE LATER!!!
		//System.out.println("CLIENT: this is my ENCRYPTED request size: " + cipherBasketSendKey.length); // TEST MESSAGE, REMOVE LATER!!!
		flush(cipherBasketSendKey);
		
		// test message 
		//System.out.println("CLIENT: my public key was sent!: " + Parser.parseByte(asyCrypto.getPublicKey())); // TEST MESSAGE, REMOVE LATER!!!
		//System.out.println("CLIENT: my public key was sent! HASH: " + Parser.parseByte(asyCrypto.getPublicKey().hashCode())); // TEST MESSAGE, REMOVE LATER!!!
		
		//returning the key I got
		return serverPublicKey;
		//authenticated.
	}
	
	public Peer requestSession(String address, PublicKey serverPublicKey) throws Exception{
		// send basket with the request
		// FORMAT: Eks(Epa(I want talk to B))
		Basket getTicketBasket = new Basket(Header.GetTicket, Parser.parseByte(address));
		byte[] getTicketCipher = asyCrypto.encrypt(Parser.parseByte(getTicketBasket)); // Integrity
		byte[] getTicketCipher2 = asyCrypto.encrypt(getTicketCipher, serverPublicKey); // Confidentiality
		
		System.out.println("CLIENT: requesting session key with " + address); // TEST MESSAGE, REMOVE LATER!!!
		System.out.println("CLIENT: encrypted in order: " + asyCrypto.getPublicKey().hashCode() + " : " + 
		serverPublicKey.hashCode()); // TEST MESSAGE, REMOVE LATER!!!
				
		flush(getTicketCipher2);
		System.out.println("CLIENT: requested."); // TEST MESSAGE, REMOVE LATER!!!
		
		// Response
		// decrypt FORMAT: Epa(Eks(ABkey + Ticket))
		byte[] resposeCipher = read();
		byte[] responseCipherBasket = asyCrypto.decrypt(resposeCipher);
		Basket responseBasket = (Basket)Parser.parseObject(asyCrypto.decrypt(responseCipherBasket, serverPublicKey));
		
		
		if (responseBasket.getHeader() == Header.SendTicket){
			byte[] basketData = responseBasket.getData();
			
			sessionKey = (SecretKey)Parser.parseObject(Arrays.copyOfRange(basketData, 0, SESSIONLENGTH)); 
			
			ticket = Arrays.copyOfRange(basketData, SESSIONLENGTH, SESSIONLENGTH + TICKETLENGTH);
			System.out.println("CLIENT: got ticket: " + ticket.hashCode()); // TEST MESSAGE, REMOVE LATER!!!
			ticket = Arrays.copyOfRange(basketData, SESSIONLENGTH, basketData.length);			
			System.out.println("CLIENT: got ticket: " + ticket.hashCode()); // TEST MESSAGE, REMOVE LATER!!!
			
			System.out.println("CLIENT: got session key: " + sessionKey.hashCode()); // TEST MESSAGE, REMOVE LATER!!!
		}
		
		socket.close();		
		//return the key
		return (new Peer(address, sessionKey, ticket));
	}
	
	private void flush(byte[] bytes) throws IOException{
		//TODO send the size of the basket
		outputStream.writeInt(bytes.length);
		outputStream.write(bytes);
		//socket.shutdownOutput();	
	}

	//do NOT used directly! no cryptography implemented
	private byte[] read() throws IOException {
		// TODO receive the size of the basket
		byte[] bytes = null;
		int length = inputStream.readInt();
		bytes = new byte[length];
		inputStream.read(bytes, 0, length);
		return bytes;
	}
	
}
