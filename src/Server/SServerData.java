package Server;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import tool.AsymmetricCrypto;

class SServerData {
	private Map<String, PublicKey> map;
	public SServerData() throws Exception {
		map = new HashMap<String, PublicKey>();
	}
	public boolean addKey(String host, PublicKey key){
		map.put(host, key);
		return true;
	}
	public PublicKey getKey(String address){
		return map.get(address);
	}
	
	
}
