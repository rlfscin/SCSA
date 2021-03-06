package tool;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class AsymmetricCrypto {

	private Cipher cipher;
	private KeyPair keypair;

	public AsymmetricCrypto() throws Exception{
		asyGenKeys();
	}

	public PublicKey getPublicKey(){		
		return this.keypair.getPublic();
	}

	private void asyGenKeys() throws Exception{
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		this.keypair = kpg.generateKeyPair();

		cipher = Cipher.getInstance("RSA");
	}

	public byte[] encrypt(byte[] plainBytes) throws Exception{
		return encrypt(plainBytes, null);
	}

	// MUST RECEIVE A PUBLIC KEY, AND ALSO ENCRYPT USING THEIR OWN PRIVATE
	public byte[] encrypt(byte[] plainBytes, PublicKey publicKey) throws Exception{		
		if (publicKey != null){
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		}else{
			cipher.init(Cipher.ENCRYPT_MODE, keypair.getPrivate());
		}

		byte[] bytes = plainBytes;
		byte[] cipherBytes = blockCipher(bytes,Cipher.ENCRYPT_MODE);		
		return cipherBytes;
	}

	public byte[] decrypt(byte[] cipherBytes) throws Exception{
		return decrypt(cipherBytes, null);
	}

	public byte[] decrypt(byte[] cipherBytes, PublicKey publicKey) throws Exception{
		if (publicKey != null){			
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
		}else{
			cipher.init(Cipher.DECRYPT_MODE, keypair.getPrivate());
		}				
		byte[] plainBytes = blockCipher(cipherBytes, Cipher.DECRYPT_MODE);
		return plainBytes;
	}

	private byte[] blockCipher(byte[] plainBytes, int mode) throws IllegalBlockSizeException, BadPaddingException{
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		byte[] toReturn = new byte[0];
		// if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
		int length = (mode == Cipher.ENCRYPT_MODE)? 100 : 128;

		// another buffer. this one will hold the bytes that have to be modified in this step
		byte[] buffer = new byte[length];

		for (int i=0; i< plainBytes.length; i++){

			// if we filled our buffer array we have our block ready for de- or encryption
			if ((i > 0) && (i % length == 0)){
				//execute the operation
				scrambled = cipher.doFinal(buffer);
				// add the result to our total result.
				toReturn = asyAppend(toReturn,scrambled);
				// here we calculate the length of the next buffer required
				int newlength = length;

				// if newlength would be longer than remaining bytes in the bytes array we shorten it.
				if (i + length > plainBytes.length) {
					newlength = plainBytes.length - i;
				}
				// clean the buffer array
				buffer = new byte[newlength];
			}
			// copy byte into our buffer.
			buffer[i%length] = plainBytes[i];
		}

		// this step is needed if we had a trailing buffer. should only happen when encrypting.
		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
		scrambled = cipher.doFinal(buffer);

		// final step before we can return the modified data.
		toReturn = asyAppend(toReturn,scrambled);

		return toReturn;
	}

	private byte[] asyAppend(byte[] prefix, byte[] suffix){
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i=0; i< prefix.length; i++){
			toReturn[i] = prefix[i];
		}
		for (int i=0; i< suffix.length; i++){
			toReturn[i+prefix.length] = suffix[i];
		}
		return toReturn;
	}

}
