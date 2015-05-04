package tool;

import java.util.HashMap;
import java.util.Map;

public class Encrypt {
	private String privateKey;
	private String publicKey;
	public Encrypt() {
		this.privateKey = "Private Key";
		this.publicKey = "Public key";
	}
	
	public String getPublicKey(){
		return publicKey;
	}
	
	public String encrypt(String text){
		return "enchypted by private key server "+ text;
	}
	
	public String encrypt(String text, String key){
		return key + text;
	}
	
	public String decrypt(String text){
		return privateKey + text;
	}
	
	public String decrypt(String text, String key){
		return key + text;
	}

}
