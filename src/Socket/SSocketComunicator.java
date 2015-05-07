package Socket;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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

	public void sendObject(Object plainObject) throws Exception{
		byte[] bytes = symCrypto.encObject(plainObject, peer.sessionKey);
		
		// using flush directly is TEMPORARY!! Basket (class) will create header, tell peer about the data type and check cryptography, ALL before flush
		flush(bytes);
	}

	public Object receiveObject() throws IOException, Exception{
		return symCrypto.decObject(read(), peer.sessionKey);		
	}

	//need to test
	public void sendFile(String filename) throws IOException{	
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

		// using flush directly is TEMPORARY!! Basket (class) will create header, tell peer about the data type and check cryptography, ALL before flush
		flush(baos.toByteArray());
	}

	//need to test
	public void receiveFile() throws IOException{
		String filename = "";///// REVIEW HERE!!!! WHERE COME THE FILENAME FROM?????

		BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(filename) );

		byte[] bytes = new byte[1024];
		InputStream is = socket.getInputStream();
		int count;

		while ((count = is.read(bytes)) > 0) {
			bos.write(bytes, 0, count);
		}
		bos.close();
	}

	public void sendText(String message) throws Exception{
		// using flush directly is TEMPORARY!! Basket (class) will create header, tell peer about the data type and check cryptography, ALL before flush
		flush(symCrypto.encText(message, peer.sessionKey));
	}

	public String receiveText() throws Exception{
		String message = symCrypto.decText(read(), peer.sessionKey);
		return message;
	}

	public void sendBytes(byte[] bytes) throws Exception{	
		// using flush directly is TEMPORARY!! Basket (class) will create header, tell peer about the data type and check cryptography, ALL before flush
		flush(symCrypto.encrypt(bytes, peer.sessionKey));	
	}

	public byte[] receiveBytes() throws Exception{
		return symCrypto.decrypt(read(), peer.sessionKey);
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
