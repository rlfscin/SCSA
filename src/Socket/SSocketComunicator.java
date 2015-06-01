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
	
	
	/*
	 *We need to talk how we gonna send the files.
	//need to test
	public void sendFile(String filename) throws Exception{	
		File f = new File(filename);
		inputStream = new FileInputStream(f);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		byte[] data = new byte[4096];
		int count = inputStream.read(data);
		while(count != -1)
		{
			dos.write(data, 0, count);
			count = inputStream.read(data);
		}

		Basket basket = new Basket(baos.toByteArray());		
		flush(symCrypto.encrypt(serialize(basket), peer.sessionKey));
	}
	*/
	//need to test, correct
	public void receiveFile() throws IOException{
		/*
		  String filename = "";///// REVIEW HERE!!!! WHERE COME THE FILENAME FROM?????


		BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(filename) );

		byte[] bytes = new byte[1024];
		InputStream is = socket.getInputStream();
		int count;

		while ((count = is.read(bytes)) > 0) {
			bos.write(bytes, 0, count);
		}
		bos.close();
		 */
	}
	
	/*
	//dangers zone 

	public void sendText(String message) throws Exception{
		// using flush directly is TEMPORARY!! Basket (class) will create header, tell peer about the data type and check cryptography, ALL before flush
		byte[] data = Parser.parseByte(message);
		Basket basket = new Basket(Header.SendData, data);
		flush(symCrypto.encrypt(Parser.parseByte(basket), peer.sessionKey));
	}

	public String receiveText() throws Exception{
		byte[] basketBytes = symCrypto.decrypt(read(), peer.sessionKey);
		Basket basket = (Basket)Parser.parseObject(basketBytes);
		String message = (String)Parser.parseObject(basket.getData());
		return message;
	}

	public void sendBytes(byte[] bytes) throws Exception{	
		// using flush directly is TEMPORARY!! Basket (class) will create header, tell peer about the data type and check cryptography, ALL before flush
		Basket basket = new Basket(Header.SendData, bytes);
		flush(symCrypto.encrypt(Parser.parseByte(basket), peer.sessionKey));	
	}

	public byte[] receiveBytes() throws Exception{
		byte[] basketBytes = symCrypto.decrypt(read(), peer.sessionKey);
		Basket basket = (Basket)Parser.parseObject(basketBytes);
		return basket.getData(); 
	}
	private boolean isUp(){
		// check working cryptography before sending bytes
		return false;
	}

	*/

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
