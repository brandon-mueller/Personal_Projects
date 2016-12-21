package me.virusbrandon.powerblock;

import me.virusbrandon.ui.ResultWindow;
import me.virusbrandon.util.UpdateDetector;

import org.bukkit.Bukkit;

public class Timer {
	Runnable timer = new Runnable() {
		@SuppressWarnings("unused")
		public void run() {
			try{
				int i = ((mode==0)?main.tick():(mode==1)?res.frmTick(0):(mode==2)?a.tick():(mode==3)?res.drawPhase():(mode==4)?res.nextPhase():(mode==5)?det.check():0);	
			}catch(Exception e1){e1.printStackTrace();}
		}	
	};
	
	private int id;
	private int mode;
	private Main main;
	private ResultWindow res;
	private JAnimate a;
	private UpdateDetector det;
	
	
	/**
	 * The Timer Constructor:
	 * 
	 * @param chest
	 * @param mode
	 * 
	 * 
	 */
	
	public Timer(Main main,int mode){
		this.main=main;
		this.mode=mode;
	}
	
	
	/**
	 * The Timer Constructor:
	 * 
	 */
	public Timer(ResultWindow res, int mode){
		this.res = res;
		this.mode = mode;
	}
	
	
	/**
	 * The Timer Constructor:
	 * 
	 */
	public Timer(JAnimate a, int mode){
		this.a = a;
		this.mode = mode;
	}
	
	
	/**
	 * The Timer Constructor:
	 * 
	 */
	public Timer(ResultWindow res, Main main,int mode){
		this.res = res;
		this.main = main;
		this.mode = mode;
	}
	
	
	/**
	 * The Timer Constructor:
	 * 
	 */
	public Timer(UpdateDetector det, int mode){
		this.det = det;
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
	 * © 2015 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
