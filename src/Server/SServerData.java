package Server;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import tool.AsymmetricCrypto;

class SServerData {
	private Map<String, PublicKey> map;
	private AsymmetricCrypto asyCrypto;
	public SServerData() throws Exception {
		map = new HashMap<String, PublicKey>();
		asyCrypto = new AsymmetricCrypto();
	}
	public boolean addKey(String host, PublicKey key){
		map.put(host, key);
		return true;
	}
	public PublicKey getKey(String address){
		return map.get(address);
	}
	
	public byte[] encrypt(byte[] plainBytes) throws Exception{
		return asyCrypto.encrypt(plainBytes);
	}

	public byte[] encrypt(byte[] plainBytes, PublicKey publicKey) throws Exception{		
		return asyCrypto.encrypt(plainBytes, publicKey);
	}

	public byte[] decrypt(byte[] cipherBytes) throws Exception{
		return asyCrypto.decrypt(cipherBytes);
	}

	public byte[] decrypt(byte[] cipherBytes, PublicKey publicKey) throws Exception{
		return asyCrypto.decrypt(cipherBytes, publicKey);
	}
	
}
