package Socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import tool.Encrypt;

public class SSocket {
	private Encrypt encrypt;
	private Socket socket;
	
	public SSocket(){
		try {
			encrypt = new Encrypt();
			this.socket = new Socket("localhost", 5999);
			(new SSocketComunicator(encrypt, socket)).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
