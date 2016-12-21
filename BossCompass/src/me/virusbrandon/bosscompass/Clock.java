package me.virusbrandon.bosscompass;

import me.virusbrandon.localapis.ActionBarAPI;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Clock {
	private String clock = ""; 			/* Clock Display */
	private String clkTemplate = "╻╵▍";	/* Clock Template */	
	private String ye=ChatColor.YELLOW+"",re=ChatColor.RED+"",ga=ChatColor.GRAY+"";
	private String[] clrs = new String[]{ye,re,ga};
	
	/**
	 * The Clock Constructor.
	 * 
	 */
	public Clock(){
		int i = 6;
		for(int x=0;x<24;x++){
			i = ((i%13)==0)?1:(i%13);
			clock+=((i<10)?" ":"")+i+((((x<=5)|(x>=18))?"A":"P")+"M")+" ╻▍╻▍╻▍╻▍╻▍╻▍╻▍╻▍╵╵▍╻▍╻▍╻▍╻▍╻▍╻▍╻▍╻ ";
			i++;
		}	
	}
	
	/**
	 * Builds And Sends The Clock Out
	 * To The Player's Actionbar.
	 * 
	 * @param p
	 * @param bar
	 */
	public void sendClock(Player p, ActionBarAPI bar){
		int c = 1000;
		Location ll = p.getLocation();
		long t = ll.getWorld().getTime();
		int l =(int)((((t-c)<0)?((t-c)+24000):(t-c))/25);
		int r = (int)((((t+c)>24000)?((t+c)-24000):(t+c))/25);
		String s = clockPrep(((l>r)?clock.substring(l, clock.length())+clock.substring(0, r):clock.substring(l, r)));
		bar.sendActionBar(p, s);
	}
	
	/**
	 * Prepares The Clock View.
	 * 
	 * @param s
	 * @return
	 */
	private String clockPrep(String s){
		for(int x=0;x<clkTemplate.length();x++){
			s = s.replaceAll(clkTemplate.substring(x,x+1), clrs[x]+clkTemplate.substring(x,x+1));
		}
		return s;
	}
}
