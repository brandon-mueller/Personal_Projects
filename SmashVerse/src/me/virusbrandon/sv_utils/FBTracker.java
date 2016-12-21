package me.virusbrandon.sv_utils;

import me.virusbrandon.game.ArenaManager;
import me.virusbrandon.smashverse.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.FallingBlock;

import de.inventivegames.hologram.HologramAPI;

public class FBTracker {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				dur--;
				if(dur<=0){
					man.getMain().hM().addHolo(HologramAPI.createHologram(fb.getLocation(), re+"█"), false);
					fb.remove();
					stop();
				}
			}catch(Exception e1){
				stop();
			}
		}	
	};
	
	private FallingBlock fb;
	private ArenaManager man;
	private int id;
	private int dur = 7;
	private String re=ChatColor.RED+"";
	
	public FBTracker(ArenaManager man,FallingBlock fb){
		this.man = man;
		this.fb = fb;
		start(1);
	}
	
	/**
	 * Starts The Timer
	 * 
	 * @param delay
	 * 
	 * 
	 */
	public void start(int delay){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay, delay);
	}
	

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	public void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
