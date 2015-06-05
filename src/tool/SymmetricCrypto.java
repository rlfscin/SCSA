package tool;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricCrypto {

	private SecretKey sectionKey;

	public SymmetricCrypto(SecretKey sectionKey){
		this.sectionKey = sectionKey;
	}

	public static SecretKey generateKey(){		
		byte[] keyBytes = new byte[24];
		new Random().nextBytes(keyBytes);
		SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		return key;
	}

	public byte[] encrypt(byte[] plainBytes) throws Exception {
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, sectionKey, iv);

		final byte[] cipherBytes = cipher.doFinal(plainBytes);
		return cipherBytes;
	}

	public byte[] decrypt(byte[] cipherBytes) throws Exception {
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		decipher.init(Cipher.DECRYPT_MODE, sectionKey, iv);

		final byte[] plainBytes = decipher.doFinal(cipherBytes);
		return plainBytes;
	}

}
