package me.virusbrandon.AniMessage;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Message implements java.io.Serializable{
	private static final long serialVersionUID = 47299690L;
	private ArrayList<Block> blocks = new ArrayList<>();
	private int world;
	private int start1;
	private int start2;
	private int start3;
	private int end1;
	private int end2;
	private int end3;
	private int delay = 20; //In Minecraft Ticks! (20 Ticks == 1 Second)
	private int t1,t2,t3,t4,t5,t6;
	private int c = 0;
	private int runs = 0;
	private int[] stor;
	private int ID;
	private int toggle;
	private int dir;
	private int direct = 1;
	private String name;
	private String worldName;
	
	@SuppressWarnings("deprecation")
	Message(int world, int start1,int start2, int start3, int end1, int end2, int end3, int delay, int ID, int direct,String name){
		this.world = world;
		this.start1 = start1;
		this.start2 = start2;
		this.start3 = start3;
		this.end1 = end1;
		this.end2 = end2;
		this.end3 = end3;
		this.delay = delay;
		this.ID = ID;
		this.direct = direct;
		this.name = name;
		this.worldName = Bukkit.getServer().getWorlds().get(world).getName();
		t1 = Math.min(start1, end1);
		t2 = Math.min(start2, end2);
		t3 = Math.min(start3, end3);
		t4 = Math.max(start1, end1);
		t5 = Math.max(start2, end2);
		t6 = Math.max(start3, end3);
		stor = new int[t5-t2];
		
		for(int x = t1;x < (t4+1);x++){
			for(int z = t3;z < (t6+1); z++){
				for(int y = t2; y < (t5+1);y++){
					blocks.add(new Block(Bukkit.getServer().getWorlds().get(world).getBlockTypeIdAt(x, y, z),Bukkit.getServer().getWorlds().get(world).getBlockAt(x,y,z).getData()));
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void cycle(){
		for(int x = t1;x < (t4+1);x++){
			for(int z = t3;z < (t6+1); z++){
				for(int y = t2; y < (t5+1);y++){
					Bukkit.getServer().getWorlds().get(world).getBlockAt(x, y, z).setTypeId(blocks.get(c).getID());
					Bukkit.getServer().getWorlds().get(world).getBlockAt(x, y, z).setData(blocks.get(c).getData());
					c++;
					if(c >= blocks.size()){
						c = 0;
					}
				}
			}
		}
		c = ((t5-t2)+1)*runs;
		runs+=direct;
		if(direct == 1){
			runs = (Math.abs(runs) % (blocks.size() / ((t5-t2)+1))); 
		} else {
			if(runs < 0){
				runs = (blocks.size() / ((t5-t2)+1))-1;
			}
		}
	}
	
	public String toggle(){
		toggle++;
		toggle = (toggle%2);
		if(toggle == 1){
			return ChatColor.RED + "Sign #" + ID + " Disabled!";
		} else {
			return ChatColor.GREEN + "Sign #" + ID + " Enabled!";
		}
	}
	
	public String changeDirection(){
		dir++;
		dir = (dir%2);
		if(dir == 1){
			direct = -1;
			return ChatColor.YELLOW + "Sign #" + ID + " Flipped Directions!";
		} else {
			direct = 1;
			return ChatColor.YELLOW + "Sign #" + ID + " Flipped Directions!";
		}
	}
	
	public int getState(){
		return toggle;
	}
	
	public void startTimer(){
		toggle = 0;
	}
	
	public void stopTimer(){
		toggle = 1;
	}
	
	public int getWorld(){
		return world;
	}
	
	public int getT1(){
		return t1;
	}
	
	public int getT2(){
		return t2;
	}
	
	public int getT3(){
		return t3;
	}
	
	public int getT4(){
		return t4;
	}
	
	public int getT5(){
		return t5;
	}
	
	public int getT6(){
		return t6;
	}
	
	public int getDelay(){
		return delay;
	}
	
	public int getDirect(){
		return direct;
	}
	
	public String getName(){
		return name;
	}
	
	public void setWorldName(String name){
		this.worldName = name;
	}
	
	public String getWorldName(){
		return worldName;
	}
	
	public String getCoords(){
		return "At Coordinants: (X = " + t1 + ", Y = " + t2 + ", Z = " + t3 + ")";
	}
	
	public int getID(){
		return ID;
	}
	
	public String setName(String name){
		String tempName = this.name;
		this.name = name;
		return ChatColor.GREEN + "\nSign Name Was Changed From " + tempName + " To " + name;
	}
	
	public String setDelay(int delay){
		int temp = this.delay;
		this.delay = delay;
		return ChatColor.GREEN + "\nDelay Was Changed From " + temp + "tick(s) To " + delay + "tick(s)";
	}
	
	public void setWorld(int world){
		this.world = world;
	}
	
	public String toString(){
		return "Name: " + getName() +  " >>> Size: " + blocks.size() + " Blocks >>> Timing: " + delay + "tick(s)\nLocation: [(" + t1 + "," + t2 + "," + t3 + ") TO (" + t4 + "," + t5 + "," + t6 + ")]";
	}
}
