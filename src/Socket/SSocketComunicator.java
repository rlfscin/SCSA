package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import tool.Encrypt;

public class SSocketComunicator extends Thread {
	private Socket socket;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	private Encrypt encrypt;
	
	
	public SSocketComunicator(Encrypt encrypt, Socket socket){
		this.socket = socket;
		try {
			this.dataInput = new DataInputStream(this.socket.getInputStream());
			this.dataOutput = new DataOutputStream(this.socket.getOutputStream());
			this.encrypt = encrypt;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	}

}
