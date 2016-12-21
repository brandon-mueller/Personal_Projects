package me.virusbrandon.AniMessage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Selection {
	Location loc;
	World world;
	
	Selection(Location loc){
		this.loc = loc;
	}
	
	public int getWorldId(){
		world = loc.getWorld();
		for(int x = 0;x<Bukkit.getServer().getWorlds().size();x++){
			if(world.getName().equals(Bukkit.getServer().getWorlds().get(x).getName())){
				return x;
			}
		}
		return -1;
	}
	
	public int getX(){
		return (int) loc.getX();
	}
	
	public int getY(){
		return (int) loc.getY();
	}
	
	public int getZ(){
		return (int) loc.getZ();
	}
	
	public Location L(){
		return new Location(world,getX(),getY(),getZ());
	}
	
	public int getRegionSize(Selection other){
		int[] r = new int[]{1,1,1};
			r[0] += Math.abs(Math.max(getX(), other.getX()) - Math.min(getX(), other.getX()));
			r[1] += Math.abs(Math.max(getY(), other.getY()) - Math.min(getY(), other.getY()));
			r[2] += Math.abs(Math.max(getZ(), other.getZ()) - Math.min(getZ(), other.getZ()));
		return r[0]*r[1]*r[2];
	}
}
