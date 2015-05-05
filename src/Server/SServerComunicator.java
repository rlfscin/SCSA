package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class SServerComunicator extends Thread{
	private Socket socket;
	private SServerData sServerData;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	public SServerComunicator(Socket socket, SServerData sServerData){
		this.socket = socket;
		this.sServerData = sServerData;
		try {
			dataInput = new DataInputStream(this.socket.getInputStream());
			dataOutput = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	public void run(){
		try {
			//handShake();
			//communicate();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	/*
	private void handShake() throws IOException{
		dataOutput.writeUTF(sServerData.getPublicKey());
	}*/
	/*
	private void communicate() throws IOException{
		//while(true){
			String text = dataInput.readUTF();
			System.out.println(text);
			text  = sServerData.decrypt(text);
			System.out.println(text);
		//}
	}*/
}
