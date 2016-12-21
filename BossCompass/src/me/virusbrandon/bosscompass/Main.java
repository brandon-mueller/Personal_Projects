package me.virusbrandon.bosscompass;

import java.util.HashMap;
import java.util.UUID;

import me.virusbrandon.bosscompass.CompassSetting.Mode;
import me.virusbrandon.localapis.ActionBarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	private ActionBarAPI bar = new ActionBarAPI();
	private HashMap<UUID,Pref> prefs = new HashMap<>();
	private String wh=ChatColor.WHITE+"",bu=ChatColor.BLUE+"",gr=ChatColor.GREEN+"",re=ChatColor.RED+"",bo=ChatColor.BOLD+"";
	private Compass compass;
	private Clock clock;
	
	/**
	 * Creates New Prefs For New Player.
	 * 
	 * @param e
	 */
	@EventHandler
	public void join(PlayerJoinEvent e){
		findPref(e.getPlayer());
	}
	
	/**
	 * Removes A Player's Prefs From Memory
	 * After They Leave.
	 * 
	 * @param e
	 */
	@EventHandler
	public void leave(PlayerQuitEvent e){
		UUID k = e.getPlayer().getUniqueId();
		if(prefs.containsKey(k)){
			prefs.remove(k);
		}
	}
	
	/**
	 * Updates The Compass Or Clock
	 * When The Player Moves, Given Then
	 * They Have Sufficient Privileges.
	 * 
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(p.getItemInHand().getType()==Material.COMPASS){
			if(findPref(p).getMode() == Mode.COMPASS){
				if(p.hasPermission("BossCompass.Compass")){
					compass.sendCompass(p,bar);
				}
			} else {
				if(p.hasPermission("BossCompass.Clock")){
					clock.sendClock(p,bar);
				}
			}
		}	
	}
	
	/**
	 * Does Awesome Stuff When The Plugin
	 * First Starts Up.
	 * 
	 */
	@Override
	public void onEnable(){
		if(startupReq()){
			Bukkit.getServer().getPluginManager().registerEvents(this, this);
			compass = new Compass();
			clock = new Clock();
		} else {
			Bukkit.getConsoleSender().sendMessage(re+bo+"BossCompass Only Supports 1.8 And Up ... SORRY O_O");
			try{
				throw new InvalidServerException(re+"This Server Is Not Supported!");
			}catch(Exception e1){}
			this.getServer().getPluginManager().disablePlugin(Main.getPlugin(Main.class));
		}
	}
	
	/**
	 * Does Absolutely Nothing When The
	 * Plugin Is Being Stopped.
	 * 
	 */
	@Override
	public void onDisable(){}
	
	/**
	 * Responds To Commands That Are
	 * Directly At This Plugin.
	 * 
	 */
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		if(se instanceof Player){
			Player p = (Player)se;
			if(lbl.equalsIgnoreCase("Compass")){
				if(p.hasPermission("BossCompass.use")){
					p.sendMessage(gr+findPref((Player)se).switchMode());
				} else {
					p.sendMessage(bu+"[ "+wh+"Boss Compass"+bu+" ] "+re+"No Permission!");
				}
			}
		} else {
			se.sendMessage(bu+"This Command Is For In Game Players ONLY!");
		}
		return true;
	}
	
	/**
	 * Finds The Prefs For A Player And
	 * Creates New Ones If Prefs Don't Exist
	 * For A Given Player.
	 * 
	 * @param p
	 * @return
	 */
	private Pref findPref(Player p){
		UUID id = p.getUniqueId();
		if(prefs.containsKey(id)){
			return prefs.get(id);
		} else {
			prefs.put(id,new Pref(p));
		}
		return prefs.get(id);
	}
	
	/**
	 * Determines Whether Or Not
	 * This Software Is Being Run
	 * On A Support Server Version
	 * 
	 */
	private boolean startupReq(){
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		return(nmsver.startsWith("v1_8_")|nmsver.startsWith("v1_9_")|nmsver.startsWith("v1_10_"));
	}
}
