package Server;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

import javax.crypto.SecretKey;
import javax.xml.ws.handler.MessageContext.Scope;

import tool.AsymmetricCrypto;
import tool.Basket;
import tool.Header;
import tool.Parser;
import tool.SymmetricCrypto;

class SServerComunicator extends Thread {
	private Socket socket;
	private SServerData sServerData;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private AsymmetricCrypto asymmetricCrypto;

	public SServerComunicator(Socket socket, SServerData sServerData,
			AsymmetricCrypto asymmetricCrypto) {
		this.asymmetricCrypto = asymmetricCrypto;

		this.socket = socket;
		this.sServerData = sServerData;
		try {
			inputStream = new DataInputStream(this.socket.getInputStream());
			outputStream = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			communicate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void communicate() throws Exception {		
		while (true) {
			byte[] input = null;
			try {
				input = read();
			} catch (IOException e) {
				break;
			}
			try {
				Basket basket = (Basket) Parser.parseObject(input);
				if (basket.getHeader() == Header.GetPublicKey) {
					sendKey();
				}
				continue;
			} catch (Exception e) {
			}

			Basket basket;
			try {
				// try to convert to basket
				basket = (Basket) Parser.parseObject(asymmetricCrypto
						.decrypt(input));

				if (basket.getHeader() == Header.SendPublicKey) {
					PublicKey clientKey = (PublicKey) Parser.parseObject(basket
							.getData());
					sServerData
							.addKey(socket.getInetAddress().getHostAddress(),
									clientKey);

					// INFO MESSAGE
					System.out.println("INFO: host " + socket.getInetAddress().getHostAddress() + " authenticated.");
				}
				continue;

			} catch (Exception e) {
				System.out.println(e.getMessage()); // TEST MESSAGE, REMOVE LATER!!
				System.out.println("SERVER: The basket couldn't be read [maybe needs one more decryption]."); // TEST MESSAGE, REMOVE LATER!!
			}
			// Decrypt request
			PublicKey clientKey = sServerData.getKey(socket.getInetAddress()
					.getHostAddress());			
			
			byte[] cipherRequestBasket = asymmetricCrypto.decrypt(input);
			
			basket = (Basket) Parser.parseObject(asymmetricCrypto.decrypt(cipherRequestBasket, clientKey));

			if (basket.getHeader() == Header.GetTicket) {
				// Get request
				String targetAddress = (String) Parser.parseObject(basket.getData());
				System.out.println("SERVER: Got a ticket request to : "	+ (String) Parser.parseObject(basket.getData())); // TEST MESSAGE, REMOVE LATER!!

				// create session key
				SecretKey sessionkey = SymmetricCrypto.generateKey();
				System.out.println("SERVER: session key generated : " + sessionkey); // TEST MESSAGE, REMOVE LATER!!

				// generate ticket
				// (ABkey + Ticket)
				byte[] ticket = generateTicket(targetAddress, sessionkey);
				System.out.println("SERVER: ticket generated : " + ticket.hashCode()); // TEST MESSAGE, REMOVE LATER!!

				// send ticket - Response
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				outputStream.write(Parser.parseByte(sessionkey));
				outputStream.write(ticket);
				byte[] ticketResponse = outputStream.toByteArray();
				
				// INFO MESSAGE
				System.out.println("INFO: generating session key and ticket:" + targetAddress + "|" +
						socket.getInetAddress().getHostAddress() + " (hash:" + ticket.hashCode() + ")");

				// then send the ticket back
				Basket ticketBasket = new Basket(Header.SendTicket,	ticketResponse);
				System.out.println("SERVER: ticket response length: " +  ticketResponse.length);
				byte[] ticketBasketCipher = asymmetricCrypto.encrypt
						(asymmetricCrypto.encrypt(Parser.parseByte(ticketBasket))
								, sServerData.getKey(socket.getInetAddress().getHostAddress()));
				flush(ticketBasketCipher);

				break;
			}

		}
		if (socket != null)
			socket.close();
	}

	private void sendKey() throws IOException {
		Basket basket = new Basket(Header.SendPublicKey,
				Parser.parseByte(asymmetricCrypto.getPublicKey()));
		flush(Parser.parseByte(basket));
	}

	private byte[] generateTicket(String targetAddress, SecretKey sessionkey)
			throws IOException, Exception {
		PublicKey tarketPublicKey = sServerData.getKey(targetAddress);
		byte[] ticket = asymmetricCrypto.encrypt(Parser.parseByte(sessionkey),
				tarketPublicKey);
		return ticket;
	}

	private void flush(byte[] bytes) throws IOException {
		outputStream.writeInt(bytes.length);
		outputStream.write(bytes);
	}

	private byte[] read() throws IOException {
		byte[] bytes = null;
		int length = inputStream.readInt();
		bytes = new byte[length];
		inputStream.read(bytes, 0, length);
		return bytes;
	}
}
