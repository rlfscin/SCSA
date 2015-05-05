package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class SSocketServerComunicator extends Thread{
	private Socket socket;
	private SSocketServerData ssocketServerData;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	public SSocketServerComunicator(Socket socket, SSocketServerData ssocketServerData){
		this.socket = socket;
		this.ssocketServerData = ssocketServerData;
		try {
			dataInput = new DataInputStream(this.socket.getInputStream());
			dataOutput = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run(){
		try {
			handShake();
			communicate();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handShake() throws IOException{
		dataOutput.writeUTF(ssocketServerData.getPublicKey());
	}
	
	private void communicate() throws IOException{
		//while(true){
			String text = dataInput.readUTF();
			System.out.println(text);
			text  = ssocketServerData.decrypt(text);
			System.out.println(text);
		//}
	}
}
