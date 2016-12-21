package me.virusbrandon.HoloBeam;


import org.bukkit.Bukkit;

public class Timer {
	Runnable timer = new Runnable() {
		public void run() {
			try{
				if(mode==0){
					holo.tick();
				} else if(mode==1){
					main.checkCoolDown();
				}
			}catch(Exception e1){}
		}	
	};
	
	private int id;
	private int mode;
	private HoloObject holo;
	private Main main;
	
	/**
	 * The Timer Constructor:
	 * 
	 * 
	 */
	public Timer(HoloObject holo, int mode){
		this.holo = holo;
	}
	
	public Timer(Main main, int mode){
		this.main = main;
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
