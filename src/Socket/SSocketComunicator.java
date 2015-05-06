package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import tool.AsymmetricCrypto;
import tool.SymmetricCrypto;

public class SSocketComunicator extends Thread {
	private Socket socket;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	private SymmetricCrypto symCrypto;
	private Peer peer;

	public SSocketComunicator(SymmetricCrypto symCrypto, Socket socket, Peer peer){
		this.socket = socket;
		try {
			this.dataInput = new DataInputStream(this.socket.getInputStream());
			this.dataOutput = new DataOutputStream(this.socket.getOutputStream());
			this.symCrypto = symCrypto;
			this.peer = peer;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendFile(){
		
		//get image in bytes
		
		sendBytes(null);
	}

	public void receiveFile(){
		receiveBytes();
		
		//get bytes back to image
		
	}
	
	public void sendText(String message) throws Exception{
		sendBytes(symCrypto.encText(message, peer.sessionKey));
	}

	public String receiveText() throws Exception{
		String message = symCrypto.decText(receiveBytes(), peer.sessionKey);
		return message;
	}


	public void sendBytes(byte[] bytes){
		// use whatever is necessary to send bytes
	}

	public byte[] receiveBytes(){
		byte[] bytes = null;
		
		// use whatever is necessary to receive bytes
		
		return bytes;
	}

	/*
	public void run(){
		try {
			String publicServerKey = dataInput.readUTF();
			System.out.println(publicServerKey);
			String text = encrypt.encrypt(" Mensage from the Client to the server", publicServerKey);
			dataOutput.writeUTF(text);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}*/

}
