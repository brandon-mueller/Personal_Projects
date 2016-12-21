package me.virusbrandon.chatthing;

import java.util.HashMap;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	private HashMap<String, Player> names = new HashMap<>();
	private int id;
	private String gr = ChatColor.GREEN+"";
	
	/**
	 * This section refreshes the hashmap
	 * of online players every second
	 * 
	 */
	Runnable timer = new Runnable() { 
		public void run() {
			names.clear();
			for(Player p:Bukkit.getOnlinePlayers()){
				names.put(p.getName().toUpperCase(), p);
			}
		}
	};
	
	/**
	 * This section listens for whenever someone
	 * says something in chat, then checks to see
	 * if they mentioned another player.
	 * 
	 * Cavat: Requires that the person mentioning
	 * someone else spells the other player's name
	 * fully and correct ie: tab-complete.
	 * 
	 * @param e
	 */
	@EventHandler
	public void chat(AsyncPlayerChatEvent e){
		for(String s:names.keySet()){
			Player p = names.get(e.getMessage().toUpperCase().contains(s)?s:"");
			if((p != null)&&(!p.equals(e.getPlayer()))){
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 2.0f);
				p.sendMessage(gr+e.getPlayer().getName()+" Mentioned You!");
			}
		}
	}
	
	/**
	 * Called When The Plugin Is Started
	 * 
	 */
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		start(20);
	}
	
	/**
	 * Called When The Plugin Is Stopped
	 * 
	 */
	public void onDisable(){
		stop();
	}
	
	/**
	 * Well... There are no commands for this
	 * plugin at this time, but it's good to have
	 * this here just in case we decide to add some!
	 */
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		return true;
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
	private void stop(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
}
