package me.virusbrandon.AniMessage;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;

public class Parkour implements java.io.Serializable {
	private static final long serialVersionUID = 159489030L;
	
	private int delay = 0,delayt = 0;
	private int state = 0;
	private int dfs = 250;
	private String worldName;
	protected ArrayList<Segment> se;
	
	Parkour(World w){
		se = new ArrayList<>();
		for(int x = 0;x<dfs;x++){
			this.se.add(new Segment());
		}
		this.worldName = w.getName();
	}
	
	public void cycle(){
		if(delayt>delay){
			delayt = 0;
			for(int x = 0;x<se.size();x++){
				if(se.get(x).getBlocks().size()>0){
					se.get(x).tick();
				}
			}
		}
		delayt++;
		
	}
	public String addSeg(){
		se.add(new Segment());
		return "> Pkr > New Segment Added!";
	}
	
	public void sSeg(ArrayList<Segment> seg){
		this.se = seg;
	}
	
	public ArrayList<Segment> S(){
		return se;
	}
	
	public void setDelay(int i){
		this.delay = i;
	}
	
	public int getDelay(){
		return delay;
	}
	
	public int getState(){
		return state;
	}
	
	public void setWorldName(String name){
		this.worldName = name;
	}
	
	public String getWorldName(){
		return worldName;
	}
	
	public void setWorld(int i){
		for(int x = 0;x<se.size();x++){
			se.get(x).setWorld(i);
		}
	}
	
	public void addBlock(Selection s,int x){
		se.get(x).addBlock(s);
	}
	
	public Location L(int  i){
		return (se.get(i).getBlocks().get(0).L());
	}
	
	public String toggle(){
		if(state == 0){
			this.state = 1;
			return "> State > Disabled";
		}
		if(state == 1){
			this.state = 0;
			return "> State Enabled";
		}
		return "error";
	}
	
	public String toString(){
		return "Segments: " + se.size() +  " Parkour >>> Timing: " + delay + "tick(s)\nWORLD: "+worldName+"]";
	}
}
