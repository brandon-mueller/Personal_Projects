package me.virusbrandon.blackhole;

import me.virusbrandon.game.Arena;
import me.virusbrandon.smashverse.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import de.inventivegames.hologram.HologramAPI;

public class BlackHole {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				duration--;
				if(duration<=0){
					stop();
					target.getWorld().createExplosion(target, 3);
				} else {
					for(Player p:ar.getPlayers()){
						if(ar.getMan().calcDist(p.getLocation(), target)<5){
							ar.getMan().corDir(p,target,1,20);
							p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_DEATH, 2, .5f);
						}
					}
					ar.getMan().getMain().hM().addHolo(HologramAPI.createHologram(new Location(target.getWorld(),((target.getX()-2)+(Math.random()*4)),((target.getY()-2)+(Math.random()*4)),((target.getZ()-2)+(Math.random()*4))), bl+bo+"█"),false);
				}
			}catch(Exception e1){}
		}	
	};
	
	private Location target;
	private Arena ar;
	private int id;
	private int duration = 100;
	private String bo=ChatColor.BOLD+"",bl=ChatColor.BLACK+"";
	
	public BlackHole(Location target, Arena ar){
		this.target = target;
		this.ar = ar;
	}
	
	public void useWeapon(){
		start(1);		
	}

	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	private void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}