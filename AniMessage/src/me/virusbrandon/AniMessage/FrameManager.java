package me.virusbrandon.AniMessage;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class FrameManager implements java.io.Serializable{
	private static final long serialVersionUID = 17721729L;
	private ArrayList<ArrayList<Block>> frames = new ArrayList<>();
	private ArrayList<Block> temp;
	private int world;
	private int start1;
	private int start2;
	private int start3;
	private int end1;
	private int end2;
	private int end3;
	private int delay;
	private int ID;
	private int toggle = 1;
	private String name;
	private String worldName;
	private int t1,t2,t3,t4,t5,t6;
	private int c,c2;
	
	FrameManager(int world, int start1,int start2, int start3, int end1, int end2, int end3, int delay, int ID, String name){
		this.world = world;
		this.start1 = start1;
		this.start2 = start2;
		this.start3 = start3;
		this.end1 = end1;
		this.end2 = end2;
		this.end3 = end3;
		this.delay = delay;
		this.ID = ID;
		this.name = name;
		this.worldName = Bukkit.getServer().getWorlds().get(world).getName();
		
		t1 = Math.min(start1, end1);
		t2 = Math.min(start2, end2);
		t3 = Math.min(start3, end3);
		t4 = Math.max(start1, end1);
		t5 = Math.max(start2, end2);
		t6 = Math.max(start3, end3);
	}
	
	@SuppressWarnings("deprecation")
	public void cycle(){
		c2 = 0;
		for(int x = t1;x < (t4+1);x++){
			for(int z = t3;z < (t6+1); z++){
				for(int y = t2; y < (t5+1);y++){
					Bukkit.getServer().getWorlds().get(world).getBlockAt(x, y, z).setTypeId(frames.get(c).get(c2).getID());
					Bukkit.getServer().getWorlds().get(world).getBlockAt(x, y, z).setData(frames.get(c).get(c2).getData());
					c2++;
				}
			}
		}	
		c++;
		if(c >= frames.size()){
			c=0;
		}
	}
	
	public String addFrame(){
		if(toggle%2==1){
		temp = new ArrayList<>();
		for(int x = t1;x < (t4+1);x++){
			for(int z = t3;z < (t6+1); z++){
				for(int y = t2; y < (t5+1);y++){
					temp.add(new Block(Bukkit.getServer().getWorlds().get(world).getBlockTypeIdAt(x, y, z),Bukkit.getServer().getWorlds().get(world).getBlockAt(x,y,z).getData()));
				}
			}
		}
		frames.add(temp);
		return ChatColor.GREEN + "\nNew Frame Added To: " + getName() + "\nThis FrameSet Now Has: " + frames.size() + " Frames";
		} else {
			return ChatColor.RED + "\nToggle The Reel OFF Before Adding New Frames!";
		}
	}
	
	public String toggle(){
		if(frames.size() > 1){
			toggle++;
			toggle = (toggle%2);
			if(toggle == 1){
				stopTimer();
				return ChatColor.RED + "FrameReel #" + ID + " Disabled!";
			} else {
				startTimer();
				return ChatColor.GREEN + "FrameReel #" + ID + " Enabled!";
			}
		} else {
			return ChatColor.RED + "\nAt Least 2 Frames Are Required!";
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
	
	public String getName(){
		return name;
	}
	
	public int getWorld(){
		return world;
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
	
	public ArrayList<ArrayList<Block>>getFrames(){
		return frames;
	}
	
	public String setName(String name){
		String tempName = this.name;
		this.name = name;
		return ChatColor.GREEN + "\nFrameReel Name Was Changed From " + tempName + " To " + name;
	}
	
	public String setDelay(int delay){
		int temp = this.delay;
		this.delay = delay;
		return ChatColor.GREEN + "\nDelay Was Changed From " + temp + "tick(s) To " + delay + "tick(s)";
	}
	
	public void setWorld(int world){
		this.world = world;
	}
	
	public void setFrames(ArrayList<ArrayList<Block>> f){
		frames = f;
	}
	
	public String toString(){
		return "Name: " + getName() +  " >>> Size: " + frames.size() + " Frames >>> Timing: " + delay + "tick(s)\nLocation: [(" + t1 + "," + t2 + "," + t3 + ") TO (" + t4 + "," + t5 + "," + t6 + ")]";
	}
}
