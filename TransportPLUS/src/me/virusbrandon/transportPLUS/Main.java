package me.virusbrandon.transportPLUS;

import me.virusbrandon.localapis.ActionBarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	private RouteManager man;
	private String gr=ChatColor.GREEN+"",wh=ChatColor.WHITE+"",bo=ChatColor.BOLD+"",ul=ChatColor.UNDERLINE+"";
	private ActionBarAPI bar;
	
	@Override
	public void onEnable(){
		man = new RouteManager(this);
		try{
			man.load();
		} catch(Exception e1){
			e1.printStackTrace();
		}
		bar = new ActionBarAPI();
	}
	
	@Override
	public void onDisable(){
		man.save();
		for(Player p:Bukkit.getOnlinePlayers()){
			if(p.getOpenInventory().getTopInventory().getTitle().contains("[TP]")){
				p.closeInventory();
			}
		}
	}
	
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		if(lbl.equalsIgnoreCase("tsp")){
			Player p = (Player)se;
			try{
				if(p.hasPermission("tsp.ADMIN")){
					if(args[0].equalsIgnoreCase("Add")){
						man.addRoute(p.getLocation(), args);
						se.sendMessage(wh+bo+ul+"Add New Route:\n"+gr+"New Route Added Successfully!");
					} else if(args[0].equalsIgnoreCase("Remove")){
						man.delRoute(psI(args[1]));
						se.sendMessage(wh+bo+ul+"Remove Route:\n"+gr+"Route Removed Successfully!");
					} else if(args[0].equalsIgnoreCase("SetOrigin")){
						man.getRoutes().get(psI(args[1])).setOri(p.getLocation(),args);
					} else if(args[0].equalsIgnoreCase("SetDest")){
						man.getRoutes().get(psI(args[1])).setDest(p.getLocation(),args);
					} else if(args[0].equalsIgnoreCase("List")){
						se.sendMessage(man.showRoutes());
					} else if(args[0].equalsIgnoreCase("Help")){
						halp(se);
					} else {
						p.openInventory(man.getUi());
					}
				} else {
					p.openInventory(man.getUi());
				}
			}catch(Exception e1){
				p.openInventory(man.getUi());
			}
		}
		return true;
	}
	
	/**
	 * Just Parses To Integers
	 * 
	 * @param s
	 * @return
	 */
	protected int psI(String s){
		try{
			return Integer.parseInt(s);
		}catch(Exception e1){
			return -1;
		}
	}
	
	/**
	 * Returns The ActionBar
	 * 
	 */
	public ActionBarAPI gB(){
		return bar;
	}
	
	/**
	 * Halp Meh!
	 * 
	 * @param se
	 */
	private void halp(CommandSender se){
		se.sendMessage(wh+bo+ul+"Help Menu:\n"+gr+"/tsp Add <TAG>\n/tsp Remove <ID>\n/tsp SetOrigin <ID> <TAG>\n/tsp SetDest <ID> <TAG>\n/tsp List");
	}
}
