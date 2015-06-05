package Server;

import java.net.ServerSocket;
import java.net.Socket;

import tool.AsymmetricCrypto;

public class SServer extends Thread{
	private ServerSocket serverSocket;
	private SServerData sServerData;
	private int port;
	private AsymmetricCrypto asymmetricCrypto;
	
	public SServer(int port, boolean thread) throws Exception {
		// generate my pair of keys
		asymmetricCrypto = new AsymmetricCrypto();		
		
		boolean isThread = thread;
		this.port = port;
		
		if(isThread){
			start();
		}
		else{
			connect();
		}
	}

	private void connect(){
		try {
			sServerData = new SServerData();
			serverSocket = new ServerSocket(port);
			while(true){
				Socket socket = serverSocket.accept();				
				(new SServerComunicator(socket, sServerData, asymmetricCrypto)).start();
			}
		} catch (Exception e) {
			System.err.println(port+" is being used.");
			e.printStackTrace();
		}
	}
	
	public void run(){
		connect();
	}
}
