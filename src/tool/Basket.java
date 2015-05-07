package tool;

import java.io.Serializable;

public class Basket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1186247562604839067L;
	//enum?
	//header?
	byte[] data;
	
	public Basket(byte[] data){
		this.data = data;
	}
	
	public byte[] getData(){
		return null;
		
	}
}
