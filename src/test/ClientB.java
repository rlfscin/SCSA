package test;


import Socket.SSocket;


public class ClientB {

	public static void main(String[] args) {
		try {
			SSocket socket = null;
			if (args.length == 0){
				socket = new SSocket("localhost", 5999);
			}
			else socket = new SSocket(args[0], 5999);
			
			// request a ip to connect to host
			
			// connect to the other host
			socket.connect(args[0], 5998);
			//socket.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
