package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;

import tool.AsymmetricCrypto;
import tool.Basket;
import tool.Header;
import tool.Parser;

public class SSocketAuthenticator {
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private AsymmetricCrypto asyCrypto;
	private String serverAddress;
	private int port;
	
	public SSocketAuthenticator(AsymmetricCrypto asyCrypto, Socket socket){
		try {
			this.socket = socket;
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			this.outputStream = new DataOutputStream(this.socket.getOutputStream());
			this.asyCrypto = asyCrypto;		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PublicKey authenticate() throws UnknownHostException, IOException, ClassNotFoundException {
		
		Basket basketRequest = new Basket(Header.GetPublicKey, null);
		flush(Parser.parseByte(basketRequest));
		Basket basketResponse = (Basket) Parser.parseObject(read());
		return (PublicKey) Parser.parseObject(basketResponse.getData());
	}
	
	public byte[] requestSession(String address) throws UnknownHostException, IOException{
		
		socket.close();
		
		//return the key
		return null;
	}
	private void flush(byte[] bytes) throws IOException{
		//TODO send the size of the basket
		outputStream.write(bytes);
		socket.shutdownOutput();	
	}

	//do NOT used directly! no cryptography implemented
	private byte[] read() throws IOException{
		//TODO receive the size of the basket
		byte[] bytes = new byte[2048];
		inputStream.read(bytes);
		return bytes;
	}
	
}
