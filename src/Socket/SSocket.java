package Socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import tool.AsymmetricCrypto;

public class SSocket {
	private AsymmetricCrypto encrypt;
	private Socket socket;
	
	public SSocket() throws Exception{
		try {
			encrypt = new AsymmetricCrypto();
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
