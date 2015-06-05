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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendObject(Serializable plainObject) throws Exception{
		
		//TODO check fragments
		byte[] data = Parser.parseByte(plainObject);
		Basket basket = new Basket(Header.SendData, data);
		
		byte[] cipherBytes = symCrypto.encrypt(Parser.parseByte(basket));
		
		Basket plainBasketTEST = (Basket)Parser.parseObject(symCrypto.decrypt(cipherBytes)); // TEST
		System.out.println("Plain TExt:" + Parser.parseObject(plainBasketTEST.getData()).toString());
		
		flush(cipherBytes);
		System.out.println("sent : " + cipherBytes.hashCode()); // TEST MESSAGE
		System.out.println("Reach here");
	}

	public Serializable receiveObject() throws IOException, Exception{
		
		//TODO check fragments
		byte[] in = read();
		System.out.println("read : " + in.hashCode()); // TEST MESSAGE
		byte[] plainBasketBytes = symCrypto.decrypt(in);
		System.out.println("but not here");
		Basket basket = (Basket) Parser.parseObject(plainBasketBytes);

		return Parser.parseObject(basket.getData());
	}
	
	
	public void readTicket() throws Exception{
		byte[] ticketCipher = read();
		
		System.out.println("CLIENT: ticket read :"); // TEST MESSAGE, REMOVE LATER
		
		//peer = new Peer(address, sessionKey, ticket)
		Basket basket = (Basket) Parser.parseObject(ticketCipher);
		if(basket.getHeader() == Header.SendTicket){
			SecretKey sessionKey = (SecretKey) Parser.parseObject(asyCrypto.decrypt(basket.getData())); // ERRO ENCONTRADO
			
			symCrypto = new SymmetricCrypto(sessionKey);
			System.out.println("CLIENT: ticket HASH : " + basket.getData().hashCode()); // TEST MESSAGE, REMOVE LATER
			System.out.println("CLIENT: session key HASH : " + sessionKey.hashCode()); // TEST MESSAGE, REMOVE LATER
		}
		
	}
	
	public void sendTicket(Peer peer) throws IOException {
		Basket basket = new Basket(Header.SendTicket, peer.ticket);
		
		System.out.println("CLIENT: sending ticket HASH : " + peer.ticket.hashCode()); // TEST MESSAGE, REMOVE LATER
		
		flush(Parser.parseByte(basket));
		
		System.out.println("CLIENT: sending ticket HASH : " + basket.getData().hashCode()); // TEST MESSAGE, REMOVE LATER
		
		symCrypto = new SymmetricCrypto(peer.sessionKey);
		System.out.println("CLIENT: session key stored when sending ticket -HASH : " + peer.sessionKey.hashCode()); // TEST MESSAGE, REMOVE LATER
	}
	

	//do NOT used directly! no cryptography implemented
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
