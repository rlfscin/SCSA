package test;


import Socket.SSocket;


public class ClientA {

	public static void main(String[] args) throws Exception {
		/*
		 String address = "";

		int port = -1;
		for (String s: args) {


		}
		 */
		
		/*
		
		Basket b = new Basket(null, Parser.parseByte("Rubens Lopes"));
		byte a[] = Parser.parseByte(b);
		Basket c = (Basket) Parser.parseObject(a);
		System.out.println(Parser.parseObject(c.getData()));
		
		
		
		
		AsymmetricCrypto asc = new AsymmetricCrypto();
		AsymmetricCrypto ass = new AsymmetricCrypto();
		String nome = "Rubens";
		a = asc.encrypt(Parser.parseByte(nome),ass.getPublicKey());
		String nome1 = (String) Parser.parseObject(ass.decrypt(a));
		System.out.println(nome1);
		
		*/
		SSocket socket = null;
		if (args[1] == "") socket = new SSocket(args[0] = "localhost", 5999);
		else socket = new SSocket(args[0], 5999);
	}

}
