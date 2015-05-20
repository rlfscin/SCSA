package tool;

import java.io.Serializable;

public class Basket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1186247562604839067L;
	
	private Header header;
	private byte[] data;
	
	public Basket(Header header, byte[] data){
		this.header = header;
		this.data = data;
	}
	
	public byte[] getData(){
		return this.data;
		
	}
	
	public Header getHeader(){
		return this.header;
	}
}
