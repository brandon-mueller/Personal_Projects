package me.virusbrandon.AniMessage;

import java.util.ArrayList;

import org.bukkit.Location;

public class Segment implements java.io.Serializable {
	private static final long serialVersionUID = 129492348L;
	private ArrayList<Block> b;
	private ArrayList<TBlock> tb;
	private int index = 0;
	private int tB = 0;
	
	Segment(){
		this.b = new ArrayList<Block>();
		this.tb = new ArrayList<TBlock>();
	}
	
	@SuppressWarnings("deprecation")
	public String addBlock(Selection s){
		try{
			b.add(new Block(s.L().getWorld().getBlockAt(s.L()).getTypeId(),s.L().getWorld().getBlockAt(s.L()).getData(),s.L()));
		} catch(Exception e){return "-1";}
		return "> PKR > New Block Added To Segment!";
	}
	
	@SuppressWarnings("deprecation")
	public void tick(){
		try{
			Location l = b.get(index).L();
			l.getWorld().getBlockAt(l).setTypeId(b.get(index).getID());
			l.getWorld().getBlockAt(l).setData(b.get(index).getData());
			tb.add(new TBlock(l));
			for(int x = 0;x<tb.size();x++){
				if(tb.get(x).gT()>8){
					tb.get(x).L().getWorld().getBlockAt(tb.get(x).L()).setTypeId(tB);
					tb.remove(x);
				}
				tb.get(x).iT();
			}
			index++;
			if(index >= b.size()){index = 0;}
			//Bukkit.broadcastMessage("Index: " + index + " > Block ID: " + b.get(index).getID()+"\n> X: " + l.getX() + " Y: " + l.getY() + " Z: " + l.getZ() + " W: " + l.getWorld().getName());
		} catch(Exception e3){index = 0;}
	}
	
	public void setWorld(int i){
		for(int x = 0;x<b.size();x++){
			b.get(x).setWorld(i);
		}
	}
	
	public String getWorldName(){
		return b.get(0).getWorldName();
	}
	
	public ArrayList<Block> getBlocks(){
		return b;
	}
	
	public ArrayList<TBlock> getTBlocks(){
		return tb;
	}
	
	public String setTb(int tB){
		this.tB = tB;
		return "Trail Block Changed To ID: " + tB;
	}
}
