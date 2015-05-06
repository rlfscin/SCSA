package tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricCrypto {
	
	public SymmetricCrypto(){
		
	}
	
	public byte[] encObject(Serializable plainObject, byte[] keyBytes) throws Exception {
		byte[] cipherBytes = null;
		byte[] plainBytes =  serialize(plainObject); 			
		cipherBytes = encrypt(plainBytes, keyBytes);		
		
		return cipherBytes;
	}
	
	public Serializable decObject(byte[] cipherBytes, byte[] keyBytes)throws Exception{
		Serializable plainObject;
		byte[] plainBytes;
		plainBytes = decrypt(cipherBytes, keyBytes);
		plainObject = new String(plainBytes, "UTF-8");
		return plainObject;
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
	
	public byte[] encText(String plainText, byte[] keyBytes) throws Exception {
		byte[] cipherBytes = null;
		byte[] plainBytes = plainText.getBytes(); 			
		cipherBytes = encrypt(plainBytes, keyBytes);
		return cipherBytes;
	}

	public String decText(byte[] cipherBytes, byte[] keyBytes)throws Exception{
		String plainText = "";
		byte[] plainBytes;
		plainBytes = decrypt(cipherBytes, keyBytes);
		plainText = new String(plainBytes, "UTF-8");
		return plainText;
		
	}

	public byte[] encrypt(byte[] plainBytes, byte[] keyBytes) throws Exception {
		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		final byte[] cipherBytes = cipher.doFinal(plainBytes);
		return cipherBytes;
	}

	public byte[] decrypt(byte[] cipherBytes, byte[] keyBytes) throws Exception {
		final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
		final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		decipher.init(Cipher.DECRYPT_MODE, key, iv);

		final byte[] plainBytes = decipher.doFinal(cipherBytes);
		return plainBytes;
	}

}
