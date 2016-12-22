package me.virusbrandon.hudhb;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
				int slot = cur.close();
				PlayerInventory inv = p.getInventory();
				if(slot > 8){
					ItemStack hb = inv.getItem(inv.getHeldItemSlot());
					ItemStack st = inv.getItem(slot);
					inv.setItem(slot, hb);
					inv.setItem(inv.getHeldItemSlot(), st);
				} else {
					p.getInventory().setHeldItemSlot(slot);
				}
			}
		}catch(Exception e1){}
	}
	
	@EventHandler
	public void asm(PlayerArmorStandManipulateEvent e){
		try{
			int i = Integer.parseInt(e.getRightClicked().getCustomName());
			if((i>=0)&(i<36)){
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
	 * Commands
	 * 
	 */
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		if(se instanceof Player){
			Player p = ((Player)se);
			UUID u = p.getUniqueId();
			if(lbl.equalsIgnoreCase("HUD")){
				if(args[0].equalsIgnoreCase("Toggle")){
					if(!HUDS.containsKey(u)){
						HUDS.put(u, new HUD(p,this));
					}
					HUDS.get(u).toggleHud();
				}
			}

		} else {
			se.sendMessage("HUD Command Must Be Issues By An In Game Player.");
		}
		
		return true;
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
