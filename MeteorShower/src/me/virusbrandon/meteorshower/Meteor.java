package me.virusbrandon.meteorshower;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.Vector;

import de.inventivegames.hologram.HologramAPI;


public class Meteor implements Listener{
	Runnable timer = new Runnable() {
		public void run() {
			try{
				if(duration>0){
					Location l = b.getLocation();
					main.hM().addHolo(HologramAPI.createHologram(l, clrs[(int)(Math.random()*clrs.length)]+"████"),10,false);
					b.getWorld().playSound(l,Sound.ENTITY_ENDERDRAGON_GROWL, .1f, 2);
					duration--;
				} else {
					unreg();
				}
			}catch(Exception e1){};
		}	
	};
	
	private Main main;
	private FallingBlock b;
	private int id;
	private int duration = 200;
	private String[] clrs = new String[]{ChatColor.YELLOW+"",ChatColor.GOLD+"",ChatColor.RED+""};
	
	@EventHandler
	public void form(EntityChangeBlockEvent e){
		if(e.getEntity() instanceof FallingBlock){
			if(((FallingBlock)e.getEntity()).equals(b)){
				e.setCancelled(true);
				unreg();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public Meteor(Location l, Main main){
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		b = corDir(l.getWorld().spawnFallingBlock(l, Material.OBSIDIAN, l.getWorld().getBlockAt(l).getData()),l,1,((float)(Math.random()*6)));
		b.setDropItem(false);
		start(1);
	}
	
	public FallingBlock corDir(FallingBlock source, Location target, int inOrOut, float strength){
		Location l = source.getLocation();
		Vector v = l.toVector().subtract(target.toVector()).multiply(inOrOut).normalize();
		source.setVelocity(new Location(l.getWorld(),l.getX(),l.getY(),l.getZ(),(180-(float)Math.toDegrees(Math.atan2(v.getX(), v.getZ()))),(90-(float)Math.toDegrees(Math.acos(v.getY())))).getDirection().multiply(strength));
		return source;
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
	 * Unregister Events
	 * 
	 */
	private void unreg(){
		EntityChangeBlockEvent.getHandlerList().unregister(this);
		stop();
	}

	/**
	 * Stops The Timer
	 * 
	 * 
	 */
	private void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
