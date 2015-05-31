package test;

import Server.SServer;
import Socket.SSocket;

public class Authentication {

	public static void main(String[] args) {
		try {
			SServer sserver = new SServer(5999, false);
			SSocket socket = SSocket.getNewSSocket("localhost", 5999);
			
			System.out.println("done."); // TEST MESSAGE, REMOVE LATER!!!
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
