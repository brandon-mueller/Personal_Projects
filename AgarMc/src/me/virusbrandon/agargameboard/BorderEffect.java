package me.virusbrandon.agargameboard;

import java.util.ArrayList;

import me.virusbrandon.agarmc.Cell;
import me.virusbrandon.agarmc.Main;
import me.virusbrandon.agarutils.Hologram;

import org.bukkit.*;
import org.bukkit.entity.Player;

public class BorderEffect {
	Runnable timer = new Runnable() {
		public void run() {
			if(lvl<6){
				for(Location loc:getCircle(l,lvl,(lvl*5))){
					m.hM().addHolo(new Hologram(loc,c+"█"), 2);
				}
				lvl++;
			} else {
				stop();
			}
		}	
	};

	private Main m;
	private ChatColor c;
	private Player p;
	private Location l;
	private int id;
	private String re=ChatColor.RED+"",bo=ChatColor.BOLD+"",ye=ChatColor.YELLOW+"";
	private int lvl = 1;
	
	public BorderEffect(Cell c, Location l){
		this.m = c.getMain();
		this.c = c.getCellColor();
		this.p = c.getPlayer();
		this.l = l;
		start(2);
		m.gT().sendTitle(p, 5, 10, 5,ye+"! "+re+bo+"Map Border "+ye+"!");
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, .2f, .5f);
	}
	
	/**
	 * Returns A List Of Locations Needed
	 * To Produce A Circle That Is Visible
	 * To All Players In The Game.
	 * 
	 * @param center
	 * @param radius
	 * @param amount
	 * @return
	 */
	private ArrayList<Location> getCircle(Location center, double radius, int amount){
		World world = center.getWorld();
		double increment = ((2 * Math.PI) / amount);
		ArrayList<Location> locations = new ArrayList<Location>();
		for(int i = 0;i < amount; i++){
			 double angle = i * increment;
			 double x = center.getX() + (radius * Math.cos(angle));
			 double z = center.getZ() + (radius * Math.sin(angle));
			 locations.add(new Location(world, x, center.getY(), z));
		}
	    return locations;
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
