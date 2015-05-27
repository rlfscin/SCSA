package Server;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import tool.AsymmetricCrypto;
import tool.Basket;
import tool.Header;
import tool.Parser;
import tool.SymmetricCrypto;

class SServerComunicator extends Thread{
	private Socket socket;
	private SServerData sServerData;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private AsymmetricCrypto asymmetricCrypto;

	public SServerComunicator(Socket socket, SServerData sServerData, AsymmetricCrypto asymmetricCrypto){
		this.asymmetricCrypto = asymmetricCrypto;

		this.socket = socket;
		this.sServerData = sServerData;
		try {
			inputStream = new DataInputStream(this.socket.getInputStream());
			outputStream = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	public void run(){
		try {
			communicate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void communicate() throws Exception{
		while(true){
			byte[] input = read();
			try{
				Basket basket = (Basket) Parser.parseObject(input);
				if(basket.getHeader() == Header.GetPublicKey){
					sendKey();
					
					//test message
					System.out.println("SERVER: GetPublicKey: sending key:  " + asymmetricCrypto.getPublicKey()); // TEST MESSAGE, REMOVE LATER!! 
				}
				continue;
			} catch (ClassNotFoundException e){
				//empty
			}
			byte[] cipherBasket = sServerData.decrypt(input);
			// treat if not possible to convert to basket
			Basket basket = (Basket) Parser.parseObject(cipherBasket);
			if(basket.getHeader() == Header.SendPublicKey){
				PublicKey clientKey = (PublicKey) Parser.parseObject(basket.getData());
				sServerData.addKey(socket.getInetAddress().getHostAddress(), clientKey);
				
				//test message
				System.out.println("SERVER: SendPublicKey: storing key: " + socket.getInetAddress().getHostAddress()); // TEST MESSAGE, REMOVE LATER!!
			}
			else if(basket.getHeader() == Header.GetTicket){
				String targetAddress = (String) Parser.parseObject(basket.getData());
				PublicKey tarketPublicKey = sServerData.getKey(targetAddress);
				System.out.println("SERVER: Got a ticket request to : " + (String) Parser.parseObject(basket.getData()) ); // TEST MESSAGE, REMOVE LATER!!

				// create ticket and then (session key + ticket)				
				SymmetricCrypto symmetricCrypto = new SymmetricCrypto();
				SecretKey sessionkey = symmetricCrypto.generateKey();
				System.out.println("SERVER: session key generated : " + sessionkey); // TEST MESSAGE, REMOVE LATER!!
				
				byte[] ticket = generateTicket(tarketPublicKey, sessionkey);
				System.out.println("SERVER: ticket generated : " + (String) Parser.parseObject(ticket)); // TEST MESSAGE, REMOVE LATER!!
				
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				outputStream.write( Parser.parseByte(sessionkey) );
				outputStream.write( ticket );
				byte[] ticketResponse = outputStream.toByteArray( );
				// response should be 48 bytes long ( 24 session + 24 ticket)

				System.out.println("SERVER: response generated : " + (String) Parser.parseObject(ticketResponse)); // TEST MESSAGE, REMOVE LATER!!
				
				// then send the ticket back
				sendTicket(ticketResponse);

				break;
			}
		}
		socket.close();
	}

	private void sendKey() throws IOException{
		Basket basket = new Basket(Header.SendPublicKey, Parser.parseByte(asymmetricCrypto.getPublicKey()));
		flush(Parser.parseByte(basket));
	}


	private void sendTicket(byte[] ticketResponse) throws IOException, Exception{
		Basket basket = new Basket(Header.SendTicket, ticketResponse);
		flush(asymmetricCrypto.encrypt(Parser.parseByte(basket)));
	}

	private byte[] generateTicket(PublicKey targetPublicKey, SecretKey sessionkey) throws IOException, Exception{		
		byte[] ticket = asymmetricCrypto.encrypt(Parser.parseByte(sessionkey), targetPublicKey);
		return ticket;
	}

	private void flush(byte[] bytes) throws IOException{
		//TODO send the size of the basket
		outputStream.write(bytes);
		socket.shutdownOutput();	
	}

	//do NOT used directly! no cryptography implemented
	private byte[] read() {
		//TODO receive the size of the basket
		byte[] bytes = new byte[2048];
		try {
			inputStream.read(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
}
