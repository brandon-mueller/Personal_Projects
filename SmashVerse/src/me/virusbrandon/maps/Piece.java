package me.virusbrandon.maps;

import org.bukkit.block.Block;

/**
 * This Object is strictly for storing
 * pieces of a Super SmashVerse Map.
 * 
 * Is only capable of storing simple
 * block information and will not keep
 * track of blocks states and other
 * attributes that are unique only to
 * select blocks.
 * 
 * The information other than the block
 * id and data values are not locations,
 * but are location offsets, that refer
 * to another location.
 * 
 * @author Brandon
 *
 */

public class Piece implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private int blockID;
	private byte data;
	private int relativeX;
	private int relativeY;
	private int relativeZ;
	
	@SuppressWarnings("deprecation")
	public Piece(Block blk,int x, int y, int z){
		this.blockID = blk.getTypeId();
		this.data = blk.getData();
		this.relativeX = x;
		this.relativeY = y;
		this.relativeZ = z;
	}
	
	public int getID(){
		return blockID;
	}
	
	public byte getData(){
		return data;
	}
	
	public int X(){
		return relativeX;
	}
	
	public int Y(){
		return relativeY;
	}
	
	public int Z(){
		return relativeZ;
	}	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}