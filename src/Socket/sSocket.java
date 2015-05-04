package Socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import tool.Encrypt;

public class sSocket {
	private Encrypt encrypt;
	private Socket socket;
	
	public sSocket(){
		try {
			encrypt = new Encrypt();
			this.socket = new Socket("localhost", 5999);
			(new sSocketComunicator(encrypt, socket)).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
