package me.virusbrandon.AniMessage;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Block implements java.io.Serializable{
	private static final long serialVersionUID = 18938853L;
	private int ID;
	private byte data;
	private int x;
	private int y;
	private int z;
	private int w;
	private String worldName = "";
	
	Block(int ID, byte data){
		this.ID = ID;
		this.data = data;
	}
	
	Block(int ID, byte data, Location l){
		this.ID = ID;
		this.data = data;
		this.x = (int)l.getX();
		this.y = (int)l.getY();
		this.z = (int)l.getZ();
		for(int a = 0;a<Bukkit.getWorlds().size();a++){
			if(l.getWorld().getName().equalsIgnoreCase(Bukkit.getWorlds().get(a).getName())){
				w = a;
				this.worldName = Bukkit.getWorlds().get(a).getName();
			}
		}
	}
	
	public void setWorld(int i){
		this.w = i;
	}
	
	public int getID(){
		return ID;
	}
	
	public byte getData(){
		return data;
	}
	
	public String getWorldName(){
		return worldName;
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
	
	public Location L(){
		return new Location(Bukkit.getWorlds().get(w),X(),Y(),Z());
	}
}
