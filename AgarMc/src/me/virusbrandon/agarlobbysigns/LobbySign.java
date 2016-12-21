package me.virusbrandon.agarlobbysigns;


import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.util.Vector;

import me.virusbrandon.agarmc.Main;

public class LobbySign{
	private LobbySignManager man;
	private Location loc;
	private Vector v;
	private Block b;
	private Sign sign;
	private String bl=ChatColor.BLACK+"",gr=ChatColor.GREEN+"",bo=ChatColor.BOLD+"";
	private int id;
	private 
	
	Runnable timer = new Runnable() {
		public void run() {
			stop();
			man.addSign(getSign());
		}
	};
	
	public LobbySign(Location loc, LobbySignManager man){
		this.loc = loc;
		this.man = man;
		this.b = loc.getWorld().getBlockAt(loc);
		this.v = b.getLocation().getDirection();
		setText(new String[]{bl+bo+"Sign",bl+bo+"Detected..","",gr+bo+"Using It"});
		start((int)(Math.random()*100));
	}
	
	protected void setText(String[] text){
		try{
			sign = ((Sign)loc.getWorld().getBlockAt(loc).getState());
			for(int x=0;x<4;x++){
				sign.setLine(x, text[x]);
			}
		}catch(Exception e1){
			b.setType(Material.WALL_SIGN);
			b.getLocation().setDirection(v);	
			man.addError();
		}
		sign.update();
	}
	
	public Location getLoc(){
		return loc;
	}
	
	public LobbySign getSign(){
		return this;
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