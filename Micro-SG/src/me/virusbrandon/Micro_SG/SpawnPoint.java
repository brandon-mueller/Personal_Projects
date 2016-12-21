package me.virusbrandon.Micro_SG;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnPoint implements java.io.Serializable{
	private static final long serialVersionUID = 139893L;
	
	int x,y,z,w;
	String wn;
	float yaw,pitch;
	
	SpawnPoint(Arena arena, Location l){
		this.x = (arena.getM().getAMX()+(l.getBlockX()-arena.getM().getAMX()));
		this.y =  l.getBlockY();
		this.z = (arena.getM().getAMZ()+(l.getBlockZ()-arena.getM().getAMZ()));
		this.yaw = l.getYaw();
		this.pitch = l.getPitch();
		this.wn=l.getWorld().getName();
		for(int a = 0;a<Bukkit.getWorlds().size();a++){
			if(Bukkit.getWorlds().get(a).getName().equalsIgnoreCase(l.getWorld().getName())){
				this.w = a;
			}
		}
	}
	
	public Location getSpawnPoint(int spawnpoint){
		return new Location(Bukkit.getWorlds().get(w),x+(150*spawnpoint),y,z,yaw,pitch);
	}
	
	public String getWN(){
		return wn; 
	}
	
	public void setW(int w){
		this.w=w;
	}
}
