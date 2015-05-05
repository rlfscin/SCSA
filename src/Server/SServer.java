package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SServer {
	private ServerSocket serverSocket;
	private SServerData sSerterData;
	private int port;

	public SServer(int port){
		this.port = port;
		sSerterData = new SServerData();
		try {
			serverSocket = new ServerSocket(this.port);
			while(true){
				Socket socket = serverSocket.accept();
				(new SServerComunicator(socket, sSerterData)).start();
			}
		} catch (IOException e) {
			System.err.println(this.port+" is being used.");
			e.printStackTrace();
		}
	}
}
