package me.virusbrandon.MC_Vegas;

public class Block implements java.io.Serializable{
	private static final long serialVersionUID = 18439939L;
	private int id;
	private byte data;
	
	Block(int id,byte data){
		this.id = id;
		this.data = data;
	}
	
	public int getID(){
		return id;
	}
	
	public byte getData(){
		return data;
	}
}
