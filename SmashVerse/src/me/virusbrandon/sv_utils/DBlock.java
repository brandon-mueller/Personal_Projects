package me.virusbrandon.sv_utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class DBlock {
	private int id;
	private byte data;
	private Location loc;
	
	@SuppressWarnings("deprecation")
	public DBlock(Block b){
		this.loc = b.getLocation();
		this.id = b.getTypeId();
		this.data = b.getData();
	}
	
	public Location getLocation(){
		return loc;
	}
	
	public int getTypeId(){
		return id;
	}
	
	public byte getData(){
		return data;
	}
	
	@SuppressWarnings("deprecation")
	public void setBlock(){
		loc.getWorld().getBlockAt(loc).setTypeIdAndData(id, data,true);
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}