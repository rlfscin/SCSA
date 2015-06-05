package test;


import Socket.SSocket;


public class ClientA {

	public static void main(String[] args) {
		try {
			SSocket socket = null;
			if (args.length == 0) socket = SSocket.getNewSSocket(args[0] = "localhost", 5999);
			else socket = SSocket.getNewSSocket(args[0], 5999);
			socket.listen(5998);
			System.out.println(socket.readString());
			System.out.println(socket.readInt());
			socket.sendString("Sending file");
			socket.sendFile("Rubens SCSA.txt");
			System.out.println(socket.readString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
