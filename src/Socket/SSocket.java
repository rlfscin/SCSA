package Socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import tool.AsymmetricCrypto;
import tool.SymmetricCrypto;

public class SSocket {
	private AsymmetricCrypto asyCrypto;
	private SymmetricCrypto symCrypto;
	private Socket socket;
	private ArrayList<Peer> peers;
	
	public SSocket() throws Exception{
		try {
			asyCrypto = new AsymmetricCrypto();
			this.socket = new Socket("localhost", 5999);
			(new SSocketComunicator(asyCrypto, socket)).start();
			symCrypto = new SymmetricCrypto();
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
