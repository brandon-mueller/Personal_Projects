package me.virusbrandon.Block_Launcher;

import org.bukkit.entity.Player;

public class Cooldown {
	private Player p;
	private int time;
	
	Cooldown(Player p){
		this.p=p;
	}
	
	public void tick(){
		this.time+=1;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	public int getTime(){
		return time;
	}
}
