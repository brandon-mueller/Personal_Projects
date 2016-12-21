package me.virusbrandon.AniMessage;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TBlock implements java.io.Serializable{
	private static final long serialVersionUID = 13657457L;
	private int x;
	private int y;
	private int z;
	private int w;
	private int t = 0;;
	
	TBlock(Location l){
		this.x = (int)l.getX();
		this.y = (int)l.getY();
		this.z = (int)l.getZ();
		for(int a = 0;a<Bukkit.getWorlds().size();a++){
			if(l.getWorld().getName().equalsIgnoreCase(Bukkit.getWorlds().get(a).getName())){
				this.w = a;
			}
		}
	}
	
	public void iT(){
		t++;
	}
	
	public int gT(){
		return t;
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
