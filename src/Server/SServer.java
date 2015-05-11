package Server;

import java.net.ServerSocket;
import java.net.Socket;

public class SServer extends Thread{
	private ServerSocket serverSocket;
	private SServerData sSerterData;
	private int port;
	
	public SServer(String args[]) {
		boolean thread = false;
		port = 5999;
		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-p")){
				i++;
				port  = Integer.parseInt(args[i]);
			}
			else if(args[i].equals("-t")){
				thread = true;
			}
		}
		
		if(thread){
			start();
		}
		else{
			connect();
		}
	}

	private void connect(){
		try {
			sSerterData = new SServerData();
			serverSocket = new ServerSocket(port);
			while(true){
				Socket socket = serverSocket.accept();
				(new SServerComunicator(socket, sSerterData)).start();
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
