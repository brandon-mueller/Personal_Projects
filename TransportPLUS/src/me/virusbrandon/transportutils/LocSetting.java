package me.virusbrandon.transportutils;

import me.virusbrandon.transportPLUS.Main;
import me.virusbrandon.transportPLUS.Route;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.inventivegames.hologram.HologramAPI;

public class LocSetting{
	private Route route;
	private Location loc;
	private String type;
	private LocTimer timer;
	
	public LocSetting(Route route, Location loc, String type){
		this.route = route;
		this.loc = loc;
		this.type = type;
		timer = new LocTimer(this);
		timer.start(5);
	}
	
	public void tick(){
		route.rM().hM().addHolo(HologramAPI.createHologram(new Location(loc.getWorld(),loc.getX()+(-2.00+(Math.random()*4.00)),loc.getY()+(-2+(Math.random()*4.00)),loc.getZ()+(-1.00+(Math.random()*2.00))),type));
	}
	
	public LocTimer getTimer(){
		return timer;
	}
	
	public Location getLocation(){
		return loc;
	}

	public class LocTimer {
		Runnable timer = new Runnable() {
			public void run() {
				try{
					set.tick();
				}catch(Exception e1){e1.printStackTrace();}
			}	
		};
		private LocSetting set;
		private int id;
		
		/**
		 * The Timer Constructor:
		 * 
		 * @param chest
		 * @param mode
		 * 
		 * 
		 */
		
		public LocTimer(LocSetting set){
			this.set = set;
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
		
		public void start(int delay1, int delay2){
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), timer, delay1, delay2);
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

}

