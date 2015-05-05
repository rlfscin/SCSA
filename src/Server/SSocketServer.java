package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SSocketServer {
	private ServerSocket serverSocket;
	private SSocketServerData ssocketSerterData;
	private int port;

	public SSocketServer(int port){
		this.port = port;
		ssocketSerterData = new SSocketServerData();
		try {
			serverSocket = new ServerSocket(this.port);
			while(true){
				Socket socket = serverSocket.accept();
				(new SSocketServerComunicator(socket, ssocketSerterData)).start();
			}
		} catch (IOException e) {
			System.err.println(this.port+" is being used.");
			e.printStackTrace();
		}
	}
}
