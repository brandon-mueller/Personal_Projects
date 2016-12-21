package me.virusbrandon.Block_Launcher;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Witch {
	Player p;
	int time=0;
	
	Witch(Player p){
		this.p=p;
		p.setVelocity(new Vector(0,2,0));
	}
	
	public void tick(){
		p.playSound(p.getLocation(),Sound.GHAST_SCREAM,.1f,2.0f);
		p.setVelocity(p.getLocation().getDirection().multiply(1.5));
		time++;
	}
	
	public int getTime(){
		return time;
	}
	
	public Player getWitch(){
		return p;
	}
}
