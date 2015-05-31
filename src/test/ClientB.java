package test;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import Socket.SSocket;




public class ClientB {

	public static void main(String[] args) {
		try {
			SSocket socket = null;
			if (args.length == 0){
				socket = SSocket.getNewSSocket("localhost", 5999);
			}
			else socket = SSocket.getNewSSocket(args[0], 5999);
			
			// request a ip to connect to host
			
			// connect to the other host
			System.out.println("Type your target: " + System.lineSeparator());
			BufferedReader reader= new BufferedReader(new InputStreamReader(System.in));
			socket.connect(reader.readLine(), 5998);
			socket.sendString("Rubens Lopes de Farias Silva");
			//socket.disconnect();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
