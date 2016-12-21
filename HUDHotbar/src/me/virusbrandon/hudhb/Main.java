package me.virusbrandon.hudhb;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	private HashMap<UUID,HUD> HUDS = new HashMap<>();
	private ActionBarAPI bar;
	
	/**
	 * Handles Player Movement
	 * 
	 * @param e
	 */
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		if(HUDS.containsKey(u)){
			HUD cur = HUDS.get(u);
			if(cur.isOpen()){
				cur.select(cur.getSelected(-175+p.getLocation().getYaw()));
			}
		}
	}
	
	/**
	 * Fires When Player Sneaks Or
	 * Stops Sneaking
	 * 
	 * @param e
	 */
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e){
		try{
			Player p = e.getPlayer();
			UUID u = p.getUniqueId();
			if(!HUDS.containsKey(u)){
				HUDS.put(u, new HUD(p,this));
			}
			HUD cur = HUDS.get(u);
			if(!p.isSneaking()){
				cur.open(p.getLocation().getDirection());
			} else {
				p.getInventory().setHeldItemSlot(cur.close());
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void asm(PlayerArmorStandManipulateEvent e){
		try{
			int i = Integer.parseInt(e.getRightClicked().getCustomName());
			if((i>=0)|(i<9)){
				e.setCancelled(true);
			}
		}catch(Exception e1){}
	}
	
	/**
	 * Closes HUD When Player Dies
	 * 
	 * @param e
	 */
	@EventHandler
	public void death(PlayerDeathEvent e){
		Player p = (Player)e.getEntity();
		UUID u = p.getUniqueId();
		if(HUDS.containsKey(u)){
			HUD h = HUDS.get(u);
			if(h.isOpen()){
				h.close();
			}
		}
	}
	
	/**
	 * Closes HUD When Player
	 * Leaves The Server
	 * 
	 * @param e
	 */
	@EventHandler
	public void quit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		if(HUDS.containsKey(u)){
			HUD h = HUDS.get(u);
			h.close();
			HUDS.remove(u);
		}
	}
	
	/**
	 * Plugin Started...
	 * 
	 */
	@Override
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		bar = new ActionBarAPI();
	}
	
	/**
	 * Plugin Stopped...
	 * 
	 */
	@Override
	public void onDisable(){
		for(HUD h:HUDS.values()){
			h.close();
		}
		HUDS.clear();
	}
	
	/**
	 * Returns Reference To
	 * The ActionBar
	 * 
	 */
	public ActionBarAPI getBar(){
		return bar;
	}
}
