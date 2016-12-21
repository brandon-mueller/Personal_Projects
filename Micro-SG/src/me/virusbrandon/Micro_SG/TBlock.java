package me.virusbrandon.Micro_SG;

import org.bukkit.block.Block;

public class TBlock implements java.io.Serializable{
	private static final long serialVersionUID = 103849399L;
	
	int x;
	int y;
	int z;
	int type;
	byte data;
	
	@SuppressWarnings("deprecation")
	TBlock(int x,int y,int z,Block b){
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = b.getTypeId();
		this.data = b.getData();
	}
	
	public int X(){
		return x;
	}
	
	public int Y(){
		return y;
	}
	
	public int Z(){
		return z;
	}
	
	public int getType(){
		return type;
	}
	
	public byte getData(){
		return data;
	}
}
