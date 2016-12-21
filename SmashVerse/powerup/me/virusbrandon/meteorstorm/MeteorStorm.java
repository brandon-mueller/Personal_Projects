package me.virusbrandon.meteorstorm;

import me.virusbrandon.game.Arena;
import me.virusbrandon.smashverse.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MeteorStorm {
	Runnable timer = new Runnable() {
		public void run() {
			if(duration > 0){
				Location l = ar.getSpawn();
				new Meteor(new Location(l.getWorld(),(Math.random()>.5)?l.getX()-(Math.random()*50):l.getX()+(Math.random()*50),(l.getY()+25)+(Math.random()*25),(Math.random()>.5)?l.getZ()-(Math.random()*50):l.getZ()+(Math.random()*50)),ar);
				duration--;
			} else {
				ar.setStorming(null);
				stop();
			}
		}	
	};
	
	private int id;
	private int duration = 40;
	private Arena ar;
	
	public MeteorStorm(Arena ar){
		this.ar = ar;
		start(7);
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
