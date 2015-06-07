package test;


import java.util.Scanner;

import Socket.SSocket;


public class ClientA {

	public static void main(String[] args) {
		try {
			SSocket socket = null;
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			if (args.length == 0) socket = SSocket.getNewSSocket(args[0] = "localhost", 5999);
			else socket = SSocket.getNewSSocket(args[0], 5999);
			socket.listen(5998);
			System.out.println("Data received: " + socket.readString());
			System.out.println("Data received: " +socket.readInt());
			scanner.nextLine();
			socket.sendString("Sending file");
			scanner.nextLine();
			socket.sendFile("Rubens SCSA.txt");
			scanner.nextLine();
			System.out.println("Data received: " +socket.readString());
			scanner.nextLine();
			socket.disconnect();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
