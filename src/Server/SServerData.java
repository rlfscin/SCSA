package Server;

import java.util.HashMap;
import java.util.Map;

import tool.AsymmetricCrypto;

class SServerData {
	private Map<String, String> map;
	AsymmetricCrypto encrypt;
	public SServerData() throws Exception {
		map = new HashMap<String, String>();
		encrypt = new AsymmetricCrypto();
	}
	public synchronized boolean addKey(String host, String key){
		map.put(host, key);
		return true;
	}
	public synchronized String getKey(String host){
		return map.get(host);
	}
	/*
	public String getPublicKey(){
		return encrypt.getPublicKey();
	}
	
	public String decrypt(String text){
		return encrypt.decrypt(text);
	}
	*/
}
