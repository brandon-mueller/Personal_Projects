package me.virusbrandon.HoloBeam;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoolDown {
	private Player p;
	private long time;
	private long start;
	private float progress = 0.0f;
	private String cm;
	private Main main;
	private String bl=ChatColor.BLACK+"",gr=ChatColor.GREEN+"";
	
	public CoolDown(Player p, int time, Main main, String cm){
		this.p = p;
		this.main = main;
		this.start = System.currentTimeMillis();
		this.time = (System.currentTimeMillis()+time);
		this.cm = cm;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	public long getTime(){
		return time;
	}
	
	public boolean isCooled(long newTime){
		return((newTime>time)?(newTime>time):(newTime<time));
	}
	
	public String getCM(){
		return cm;
	}
	
	public String getProgress(){
		long now = System.currentTimeMillis();
		this.progress = (((now-start)*1.00f)/((time-start)*1.00f));
		return getBar(progress);
	}
	
	/**
	 * Returns A Progress Bar Representation Of
	 * The Amount Of Space Used Out Of Total
	 * Available Space In A User's Bottomless
	 * Chest
	 * 
	 * @param usedRatio
	 * @return
	 * 
	 * 
	 */
	private String getBar(float usedRatio){
		String bar = "";
		bar+=main.getFact().draw(gr+"▍", (int)Math.floor(usedRatio/((float)(1.00/50))),"");
		bar+=main.getFact().draw(bl+"▍",(int)(50-Math.floor(usedRatio/((float)(1.00/50))))-1, "");
		return gr+bar;
	}
}
