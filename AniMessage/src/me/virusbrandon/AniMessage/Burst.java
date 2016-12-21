package me.virusbrandon.AniMessage;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

public class Burst implements java.io.Serializable{
	private static final long serialVersionUID = 193889325L;
	private int x,y,z,w;
	private float bH = 3;
	private int state = 0;
	private int delay = 0;
	private int sdelay = 0;
	private int sign = 0;
	private int sign1 = 0;
	private String worldName;
	private int[] blocks;
	private Random r = new Random();
	
	Burst(Selection s, int ... blocks){
		this.x = s.getX();
		this.y = s.getY();
		this.z = s.getZ();
		for(int a = 0;a<Bukkit.getWorlds().size();a++){
			if(Bukkit.getWorlds().get(a).getName().equalsIgnoreCase(s.L().getWorld().getName())){
				w = a;
				this.worldName = Bukkit.getWorlds().get(a).getName();
				break;
			}
		}
		this.blocks = blocks;
	}
	
	public void cycle(){
		sdelay++;
	    if(sdelay>delay){
	    	sdelay = 0;
	    	sign = r.nextInt(2);if(sign==0){sign = -1;}else{sign = 1;}
	    	sign1 = r.nextInt(2);if(sign1==0){sign1 = -1;}else{sign1 = 1;}
	    	float n3 = r.nextFloat()*bH;
	    	Byte blockData = 0x0;
	    	@SuppressWarnings("deprecation")
	    	FallingBlock fb = L().getWorld().spawnFallingBlock(L(), blocks[r.nextInt(blocks.length)], blockData);
	    	fb.setVelocity(new Vector(sign*(.02+(r.nextInt(10)*.03)),n3,sign1*(.02+(r.nextInt(10)*.03))));
	    	fb.setDropItem(false);
	    }
	}
	
	public String toggle(){
		state++;
		this.state = (state%2);
		if(state == 0){return "> Enabled";}else{return "> Disabled";}
	}
	
	public int getState(){
		return state;
	}
	
	public Location L(){
		return new Location(Bukkit.getWorlds().get(w),x,y,z);
	}
	
	public void setWorldName(String name){
		this.worldName = name;
	}
	
	public String getWorldName(){
		return worldName;
	}
	
	public void setWorld(int w){
		this.w = w;
	}
	
	public float getBH(){
		return bH;
	}
	
	public String setBH(float bh){
		this.bH = bh;
		return "Block Height Set To: " + bh;
	}
	
	public int getDelay(){
		return delay;
	}
	
	public String setDelay(int delay){
		this.delay = delay;
		return "Burst Delay Set To: " + delay;
	}
	
	public String toString(){
		return "Block Height: " + getBH() +  " Burst >>> Timing: " + delay + "tick(s)\nLocation: [(" + x + "," + y + "," + z + ") IN WORLD "+worldName+"]";

	}
	
}
