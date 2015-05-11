package test;

import static org.junit.Assert.*;

import org.junit.Test;

import Server.AuthenticationServer;
import Server.SServer;
import Socket.SSocket;

public class Transfering {
	
	SServer server;
	SSocket client1;
	
	public void setUp(){
		String args[] = new String[0];
		//server = new SServer(args);
		client1 = new SSocket("localhost", 5999);
	}


	@Test
	public void testGetPublicKey(){
		//server.
	}
}
