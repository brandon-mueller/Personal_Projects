package me.virusbrandon.agarutils;

import me.virusbrandon.agarmc.*;

import org.bukkit.*;

public class GameOverRoutine {
	Runnable timer = new Runnable() {
		public void run() {
			if(delay>0){
				delay--;
			} else {
				p.done(false);
				stop();
			}
		}	
	};
	
	private int id;
	private int delay = 100;
	private AgarPlayer p;
	private String wh=ChatColor.WHITE+"",dg=ChatColor.DARK_GRAY+"",re=ChatColor.RED+"",gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"";
	
	public GameOverRoutine(AgarPlayer p){
		this.p = p;
		p.setEnd(System.currentTimeMillis());
		if(!p.isAI()){
			p.getMain().gT().sendFullTitle(p.getPlayer(), 50, 100, 20, re+bo+"Wasted!",gr+bo+"Time: "+fmtdTime());
		}
		start(1);
	}
	
	private String fmtdTime(){
		long fT = ((p.getEnd() - p.getStart())/1000); 
		return wh+(fT/60)+dg+"Min "+wh+(fT%60)+dg+"Sec";
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
	protected void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */
