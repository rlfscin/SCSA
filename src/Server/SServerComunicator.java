package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

import tool.Basket;
import tool.Header;
import tool.Parser;

class SServerComunicator extends Thread{
	private Socket socket;
	private SServerData sServerData;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	public SServerComunicator(Socket socket, SServerData sServerData){
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
					System.out.println("Enviando a key "+sServerData.getPublicKey());
				}
				continue;
			} catch (ClassNotFoundException e){
			}
			byte[] data = sServerData.decrypt(input);
			Basket basket = (Basket) Parser.parseObject(data);
			if(basket.getHeader() == Header.ReceiveKey){
				PublicKey clientKey = (PublicKey) Parser.parseObject(basket.getData());
				sServerData.addKey(socket.getInetAddress().getHostAddress(), clientKey);
			}
			else if(basket.getHeader() == Header.GetTicket){
				String client = (String) Parser.parseObject(basket.getData());
				PublicKey clientKey = sServerData.getKey(client);
				System.out.println("Recebi essa chave do client "+clientKey);
				break;
			}
		}
		socket.close();
	}
	
	private void sendKey() throws IOException{
		Basket basket = new Basket(Header.SendPublicKey, Parser.parseByte(sServerData.getPublicKey()));
		flush(Parser.parseByte(basket));
	}
	
	private void flush(byte[] bytes) throws IOException{
		//TODO send the size of the basket
		outputStream.write(bytes);
		socket.shutdownOutput();	
	}

	//do NOT used directly! no cryptography implemented
	private byte[] read() throws IOException{
		//TODO receive the size of the basket
		byte[] bytes = null;
		System.out.println(inputStream == null);
		inputStream.read(bytes);
		return bytes;
	}
}
