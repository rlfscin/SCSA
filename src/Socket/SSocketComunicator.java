package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import javax.crypto.SecretKey;

import tool.AsymmetricCrypto;
import tool.Basket;
import tool.Header;
import tool.Parser;
import tool.SymmetricCrypto;

public class SSocketComunicator {
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private SymmetricCrypto symCrypto;
	private AsymmetricCrypto asyCrypto;

	public SSocketComunicator(AsymmetricCrypto asyCrypto, Socket socket){
		this.socket = socket;
		try {
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			this.outputStream = new DataOutputStream(this.socket.getOutputStream());
			this.asyCrypto = asyCrypto;
			this.symCrypto = null;
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

	public void sendObject(Serializable plainObject) throws Exception{
		byte[] data = Parser.parseByte(plainObject);
		Basket basket = new Basket(Header.SendData, data);
		
		byte[] cipherBytes = symCrypto.encrypt(Parser.parseByte(basket));
		
		Basket plainBasketTEST = (Basket)Parser.parseObject(symCrypto.decrypt(cipherBytes));
		System.out.println("INFO: sending text:" + Parser.parseObject(plainBasketTEST.getData()).toString());
		
		flush(cipherBytes);
	}

	public Serializable receiveObject() throws IOException, Exception{
		byte[] in = read();
		System.out.println("INFO: read: " + in.hashCode()); // TEST MESSAGE
		byte[] plainBasketBytes = symCrypto.decrypt(in);
		Basket basket = (Basket) Parser.parseObject(plainBasketBytes);

		return Parser.parseObject(basket.getData());
	}
	
	
	public void readTicket() throws Exception{
		byte[] ticketCipher = read();
		Basket basket = (Basket) Parser.parseObject(ticketCipher);
		if(basket.getHeader() == Header.SendTicket){
			SecretKey sessionKey = (SecretKey) Parser.parseObject(asyCrypto.decrypt(basket.getData()));
			
			symCrypto = new SymmetricCrypto(sessionKey);
		}
		
	}
	
	public void sendTicket(Peer peer) throws IOException {
		Basket basket = new Basket(Header.SendTicket, peer.ticket);
		flush(Parser.parseByte(basket));		
		symCrypto = new SymmetricCrypto(peer.sessionKey);
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
