package me.virusbrandon.energydrink;

import me.virusbrandon.game.Arena;
import me.virusbrandon.smashverse.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EnergyDrink {
	Runnable timer = new Runnable() {
		public void run() {
			speeder.setWalkSpeed(speeder.getWalkSpeed()-addedSpeed);
			stop();
		}	
	};
	
	private int id;
	private float addedSpeed = 0.0f;
	private Player speeder;
	private Arena ar;
	private String gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"";
	
	public EnergyDrink(Arena ar, Player p){
		this.ar = ar;
		this.speeder = p;
	}
	
	
	public void useWeapon(){
		speeder.sendMessage(ar.getMan().getMain().getPfx()+gr+bo+"Speed Boost!");
		if(speeder.getWalkSpeed()<1){
			speeder.setWalkSpeed(speeder.getWalkSpeed()+.2f);/* Pretty Darn Fast! */
			addedSpeed = .2f;
		}
		start(200);		
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
