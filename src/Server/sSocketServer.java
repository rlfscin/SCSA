package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class sSocketServer {
	private ServerSocket serverSocket;
	private sSocketServerData ssocketSerterData;
	private int port;

	public sSocketServer(int port){
		this.port = port;
		ssocketSerterData = new sSocketServerData();
		try {
			serverSocket = new ServerSocket(this.port);
			while(true){
				Socket socket = serverSocket.accept();
				(new sSocketServerComunicator(socket, ssocketSerterData)).start();
			}
		} catch (IOException e) {
			System.err.println(this.port+" is being used.");
			e.printStackTrace();
		}
	}
}
