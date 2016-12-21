package me.virusbrandon.magicstick;

import me.virusbrandon.smashverse.Main;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class MagicStick {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				duration--;
				if(duration<=0){
					stop();
				} else {
					p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, .1f, 2f);
					p.setVelocity(p.getLocation().getDirection().multiply(1.5));
				}
			}catch(Exception e1){}
		}	
	};
	
	private Player p;
	private int duration = 100;
	private int id;
	
	public MagicStick(Player p){
		this.p = p;
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
