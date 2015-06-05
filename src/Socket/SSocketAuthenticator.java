package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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

	//private int TICKETLENGTH = 256;
	private int SESSIONLENGTH = 152;
	
	public SSocketAuthenticator(AsymmetricCrypto asyCrypto, Socket socket){
		try {
			this.socket = socket;
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			this.outputStream = new DataOutputStream(this.socket.getOutputStream());
			this.asyCrypto = asyCrypto;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public PublicKey authenticate() throws Exception {
		//requesting service
		Basket basketRequest = new Basket(Header.GetPublicKey, null);
		flush(Parser.parseByte(basketRequest));
		
		// reading response
		Basket basketResponse = (Basket) Parser.parseObject(read());		
		PublicKey serverPublicKey = (PublicKey) Parser.parseObject(basketResponse.getData());
		
		if (basketResponse.getHeader() == Header.SendPublicKey) 
			System.out.println("Server's up. Authenticating." + System.lineSeparator());
		
		//sending my key!
		Basket basketSendKey = new Basket(Header.SendPublicKey, Parser.parseByte(asyCrypto.getPublicKey()));
		
		byte[] cipherBasketSendKey = asyCrypto.encrypt(Parser.parseByte(basketSendKey), serverPublicKey);
		flush(cipherBasketSendKey);
		
		//returning the key I got
		return serverPublicKey;
		//authenticated.
	}
	
	public Peer requestSession(String address, PublicKey serverPublicKey) throws Exception{
		// send basket with the request
		Basket getTicketBasket = new Basket(Header.GetTicket, Parser.parseByte(address));
		byte[] getTicketCipher = asyCrypto.encrypt(Parser.parseByte(getTicketBasket));
		byte[] getTicketCipher2 = asyCrypto.encrypt(getTicketCipher, serverPublicKey);
		
		System.out.println("INFO: requesting session key for " + address);
				
		flush(getTicketCipher2);		
		
		// Response
		byte[] resposeCipher = read();
		byte[] responseCipherBasket = asyCrypto.decrypt(resposeCipher);
		Basket responseBasket = (Basket)Parser.parseObject(asyCrypto.decrypt(responseCipherBasket, serverPublicKey));
		
		
		if (responseBasket.getHeader() == Header.SendTicket){
			byte[] basketData = responseBasket.getData();			
			sessionKey = (SecretKey)Parser.parseObject(Arrays.copyOfRange(basketData, 0, SESSIONLENGTH)); 			
			ticket = Arrays.copyOfRange(basketData, SESSIONLENGTH, basketData.length);						
		}
		
		socket.close();		
		System.out.println("INFO: Session key for " + address + " stored.");
		//return the key
		return (new Peer(address, sessionKey, ticket));
	}
	
	private void flush(byte[] bytes) throws IOException{
		outputStream.writeInt(bytes.length);
		outputStream.write(bytes);
	}

	private byte[] read() throws IOException {
		byte[] bytes = null;
		int length = inputStream.readInt();
		bytes = new byte[length];
		inputStream.read(bytes, 0, length);
		return bytes;
	}
	
}
