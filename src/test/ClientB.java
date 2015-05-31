package test;


import Socket.SSocket;


public class ClientB {

	public static void main(String[] args) throws Exception {
		SSocket socket = null;
		if (args[1] == "") socket = new SSocket(args[1] = "localhost", 5999);
		else socket = new SSocket(args[1], 5999);
		
		// request a ip to connect to host
		
		// connect to the other host
		//socket.connect(args[1], 5998);
		//socket.disconnect();
	}

}
