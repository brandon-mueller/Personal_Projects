package me.virusbrandon.ui;

import me.virusbrandon.smashverse.Main;

import org.bukkit.Bukkit;

public class UiTimer {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				if((mode==0)?sts.refresh():(mode==1)?adm.refresh():false){/* Pointless If */}
			}catch(Exception e1){e1.printStackTrace();}
		}	
	};
	
	private int id;
	private int mode;
	private AdminWindow adm;
	private StatusWindow sts;
	
	/**
	 * The Timer Constructor:
	 * 
	 * @param chest
	 * @param mode
	 * 
	 * 
	 */
	public UiTimer(AdminWindow adm, int mode){
		this.adm = adm;
		this.mode = mode;
	}
	
	public UiTimer(StatusWindow sts, int mode){
		this.sts = sts;
		this.mode = mode;
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