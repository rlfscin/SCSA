package tool;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricCrypto {
	
	public SymmetricCrypto(){
		
	}
	
	/**
	 * 3DES Symmetric Encryption- uses encrypt()
	 * 
	 * @param  plaintext
	 * @param  key byte array
	 * @return   encrypted byte array
	 */
	public byte[] encText(String plainText, byte[] keyBytes) throws Exception {
		byte[] cipherBytes = null;
		byte[] plainBytes = plainText.getBytes(); 			
		cipherBytes = encrypt(plainBytes, keyBytes);
		return cipherBytes;
	}

	/**
	 * 3DES Symmetric Decryption - uses decrypt()
	 * 
	 * @param  cipher byte array
	 * @param  key byte array
	 * @return   plain text message
	 */
	public String decText(byte[] cipherBytes, byte[] keyBytes)throws Exception{
		String plainText = "";
		byte[] plainBytes;
		plainBytes = decrypt(cipherBytes, keyBytes);
		plainText = new String(plainBytes, "UTF-8");
		return plainText;
	}

	/**
	 * PRIVATE
	 * 3DES Symmetric Encryption 
	 * 
	 * @param  plaintext byte array
	 * @param  key byte array
	 * @return   encrypted byte array
	 */
	public byte[] encrypt(byte[] plainBytes, byte[] keyBytes) throws Exception {
		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		final byte[] cipherBytes = cipher.doFinal(plainBytes);
		return cipherBytes;
	}

	/**
	 * PRIVATE
	 * 3DES Symmetric Decryption
	 * 
	 * @param  encrypted byte array
	 * @param  key byte array
	 * @return   plain text byte array
	 */
	public byte[] decrypt(byte[] cipherBytes, byte[] keyBytes) throws Exception {
		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		decipher.init(Cipher.DECRYPT_MODE, key, iv);

		final byte[] plainBytes = decipher.doFinal(cipherBytes);
		return plainBytes;
	}

}
