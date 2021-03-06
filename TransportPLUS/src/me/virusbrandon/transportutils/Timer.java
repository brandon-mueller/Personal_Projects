package me.virusbrandon.transportutils;

import me.virusbrandon.transportPLUS.Main;

import org.bukkit.Bukkit;

public class Timer {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				holo.tick();
			}catch(Exception e1){e1.printStackTrace();}
		}	
	};
	
	private int id;
	private HoloObject holo;
	
	/**
	 * The Timer Constructor:
	 * 
	 * @param chest
	 * @param mode
	 * 
	 * 
	 */
	
	public Timer(HoloObject holo){
		this.holo = holo;
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
	 * � 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
