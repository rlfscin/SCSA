package test;

import java.security.CryptoPrimitive;

import tool.AsymmetricCrypto;
import tool.Basket;
import tool.Parser;
import Socket.SSocket;


public class ClientA {

	public static void main(String[] args) throws Exception {
		/*
		 String address = "";

		int port = -1;
		for (String s: args) {


		}
		 */
		
		//SSocket socket = new SSocket("localhost", 5999);
		
		
		AsymmetricCrypto asy = new AsymmetricCrypto();
		
		String m = "vinicius";
		byte[] b = asy.encrypt(Parser.parseByte(m));
		String plain = (String)Parser.parseObject(asy.decrypt(b));
		System.out.println(plain);
	}

}
