package me.virusbrandon.bottomlesschests;

import org.bukkit.Bukkit;

public class Timer {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				switch(mode){
					case 1:
						try{
							main.chestStatus();
						}catch(Exception e1){}
					break;
					case 3:
						chest.initTick();
					break;
					case 6:
						chest.autoSave();
					break;
					case 9:
						main.startup();
					break;
					case 10:
						main.timeCheck();
					break;
				}
			}catch(Exception e1){e1.printStackTrace();}
		}	
	};
	
	private Chest chest;
	private int id;
	private int mode;
	private Main main;
	
	/**
	 * The Timer Constructor
	 * 
	 * @param chest
	 * @param mode
	 * 
	 * 
	 */
	public Timer(Chest chest,int mode){
		this.chest=chest;
		this.mode=mode;
	}	
	
	public Timer(Main main,int mode){
		this.main=main;
		this.mode=mode;
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
