package Socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

import tool.Basket;
import tool.SymmetricCrypto;

public class SSocketComunicator {
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private SymmetricCrypto symCrypto;
	private Peer peer;

	public SSocketComunicator(SymmetricCrypto symCrypto, Socket socket, Peer peer){
		this.socket = socket;
		try {
			this.inputStream = this.socket.getInputStream();
			this.outputStream = this.socket.getOutputStream();
			this.symCrypto = symCrypto;
			this.peer = peer;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendObject(Serializable plainObject) throws Exception{
		byte[] data = serialize(plainObject);
		Basket basket = new Basket(data);

		byte[] cipherBytes = symCrypto.encrypt(serialize(basket), peer.sessionKey);

		flush(cipherBytes);
	}

	public Object receiveObject() throws IOException, Exception{
		byte[] plainBasketBytes = symCrypto.decrypt(read(), peer.sessionKey);
		Basket basket = (Basket)deserialize(plainBasketBytes);

		return basket.getData();
	}

	//need to test
	public void sendFile(String filename) throws Exception{	
		File f = new File(filename);
		inputStream = new FileInputStream(f);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		byte[] data = new byte[4096];
		int count = inputStream.read(data);
		while(count != -1)
		{
			dos.write(data, 0, count);
			count = inputStream.read(data);
		}

		Basket basket = new Basket(baos.toByteArray());		
		flush(symCrypto.encrypt(serialize(basket), peer.sessionKey));
	}

	//need to test, correct
	public void receiveFile() throws IOException{
		/*
		  String filename = "";///// REVIEW HERE!!!! WHERE COME THE FILENAME FROM?????


		BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(filename) );

		byte[] bytes = new byte[1024];
		InputStream is = socket.getInputStream();
		int count;

		while ((count = is.read(bytes)) > 0) {
			bos.write(bytes, 0, count);
		}
		bos.close();
		 */
	}

	public void sendText(String message) throws Exception{
		// using flush directly is TEMPORARY!! Basket (class) will create header, tell peer about the data type and check cryptography, ALL before flush
		byte[] data = serialize(message);
		Basket basket = new Basket(data);
		flush(symCrypto.encrypt(serialize(basket), peer.sessionKey));
	}

	public String receiveText() throws Exception{
		byte[] basketBytes = symCrypto.decrypt(read(), peer.sessionKey);
		Basket basket = (Basket)deserialize(basketBytes);
		String message = (String)deserialize(basket.getData());
		return message;
	}

	public void sendBytes(byte[] bytes) throws Exception{	
		// using flush directly is TEMPORARY!! Basket (class) will create header, tell peer about the data type and check cryptography, ALL before flush
		Basket basket = new Basket(bytes);
		flush(symCrypto.encrypt(serialize(basket), peer.sessionKey));	
	}

	public byte[] receiveBytes() throws Exception{
		byte[] basketBytes = symCrypto.decrypt(read(), peer.sessionKey);
		Basket basket = (Basket)deserialize(basketBytes);
		return basket.getData(); 
	}

	public static byte[] serialize(Serializable obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}

	public static Serializable deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return (Serializable)is.readObject();
	}

	//do NOT used directly! no cryptography implemented
	private void flush(byte[] bytes) throws IOException{		
		outputStream.write(bytes);
		socket.shutdownOutput();	
	}

	//do NOT used directly! no cryptography implemented
	private byte[] read() throws IOException{
		byte[] bytes = null;
		inputStream.read(bytes);
		return bytes;
	}

	private boolean isUp(){
		// check working cryptography before sending bytes
		return false;
	}

}
