package me.virusbrandon.meteorshower;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MeteorShower {
	Runnable timer = new Runnable() {
		public void run() {
			if(duration > 0){
				new Meteor(new Location(l.getWorld(),(Math.random()>.5)?l.getX()-(Math.random()*50):l.getX()+(Math.random()*50),(l.getY()+25)+(Math.random()*25),(Math.random()>.5)?l.getZ()-(Math.random()*50):l.getZ()+(Math.random()*50)),main);
				duration--;
			} else {
				stop();
			}
		}	
	};
	
	private int id;
	private int duration = 100;
	private Main main;
	private Location l;
	
	public MeteorShower(Main main, Location l){
		this.main = main;
		this.l = l;
		start(4);
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
