package me.virusbrandon.smashverse;

import java.util.ArrayList;
import java.util.List;

import me.virusbrandon.game.ArenaManager;
import me.virusbrandon.localapis.ActionBarAPI;
import me.virusbrandon.localapis.GUIFactory;
import me.virusbrandon.localapis.SimpleScoreboard;
import me.virusbrandon.localapis.TitleAPI;
import me.virusbrandon.sv_utils.HoloManager;
import me.virusbrandon.ui.StatusWindow;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	private ActionBarAPI bman = new ActionBarAPI();
	private ArenaManager aman;
	private GUIFactory fact = new GUIFactory();
	private HoloManager hman = new HoloManager();
	private TitleAPI tman = new TitleAPI();
	private String aq = ChatColor.AQUA+"",bo=ChatColor.BOLD+"",dr=ChatColor.DARK_RED+"",wh=ChatColor.WHITE+"",bl=ChatColor.BLACK+"";
	private String pfx = dr+bo+"[ "+aq+bo+"SmashVerse "+dr+bo+"] "+wh;
	private StatusWindow st;
	
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player)e.getWhoClicked();
		List<String> l = new ArrayList<>();
		if(p.getOpenInventory().getTopInventory().getTitle().contains("[SV]")){
			try{
				if(p.hasPermission("sv.admin")){
					try{
						l = e.getCurrentItem().getItemMeta().getLore();
						if(l.contains(bl+"#ADMIN")){
							p.openInventory(st.getUi(1));
						} else if(l.contains(bl+"#STATUS")){
							p.openInventory(st.getUi(0));
						}
						if(l.contains(bl+"#BUILD")){
							aman.getTemplateManager().showBuildSpace(p);
						}
						aman.getClass().getDeclaredMethod((l.contains(bl+"#LOBBY")?"setLobby":(l.contains(bl+"#JOIN")?"setJoin":(l.contains(bl+"#ORIGIN")?"setOrigin":""))),Location.class).invoke(aman, p.getLocation());
					}catch(Exception e1){}
				}
				String s = e.getCurrentItem().getItemMeta().getDisplayName();
				aman.getArenas().get(Integer.parseInt(s.substring(2,s.length()))).spectate(p);
			}catch(Exception e1){}
			e.setCancelled(true);
		}
	}	
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e){
		if(e.getBlock().getType() == Material.WALL_SIGN){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e){
		try{
			Player p = e.getPlayer();
			if(aman.calcDist(p.getLocation(), aman.getJoin())<aman.getJoinDist()){
				aman.join(p);
			}
		}catch(Exception e1){}
	}
		
	@Override
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		aman = new ArenaManager(this);
		st = new StatusWindow(this);
	}
	
	@Override
	public void onDisable(){
		aman.endAllArenas(false);
		hman.remAllHolos();
		aman.getTemplateManager().saveTemplates();
		st.stopUi();
		for(Player p:Bukkit.getOnlinePlayers()){
			if(p.getOpenInventory().getTopInventory().getTitle().contains("[SV]")){
				p.closeInventory();
			}
			try{
				p.setScoreboard(new SimpleScoreboard(" ").getBlankScoreboard());
			}catch(Exception e1){}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender se, Command c, String lbl,String[] args){
		if(!(se instanceof Player)){se.sendMessage("These Commands Are For In Game Use ONLY!");return true;}
		Player p = (Player)se;
		if(lbl.equalsIgnoreCase("sv")){
			try{
				if(args[0].equalsIgnoreCase("Map")&&(p.hasPermission("sv.admin"))){
					try{
						if(args[1].equalsIgnoreCase("Save")){
							aman.getTemplateManager().saveTemplate(args);
						} else if(args[1].equalsIgnoreCase("Delete")){
							aman.getTemplateManager().getTemplates().remove(Integer.parseInt(args[2]));
							se.sendMessage(wh+bo+"Map Deleted!");
						} else {
							se.sendMessage("\n"+getPfx()+"Available \"Map\" Options:\n- /SV Map Save <Name>\n- /SV Map Delete <ID>");
						}
					}catch(Exception e1){
						se.sendMessage("\n"+getPfx()+"Available \"Map\" Options:\n- /SV Map Save <Name>\n- /SV Map Delete <ID>");
					}
				} else if(args[0].equalsIgnoreCase("Leave")){
					aman.leave(p);
				} else {
					se.sendMessage("\n"+getPfx()+"All Available Options:"+((p.hasPermission("sv.admin"))?"\n- /SV Map <ARGS>":"")+"\n- /SV Leave");
				}
			}catch(Exception e1){
				p.openInventory(st.getUi(0));
			}
		}
		return true;
	}
	
	/**
	 * Get The HoloManager
	 * 
	 * @return
	 */
	public HoloManager hM(){
		return hman;
	}
	
	/**
	 * Get The GUI Factory
	 * @return
	 */
	public GUIFactory gF(){
		return fact;
	}
	
	/**
	 * Get The Title Manager
	 * 
	 */
	public TitleAPI tM(){
		return tman;
	}
	
	/**
	 * Get The ActionBar Manager
	 * 
	 */
	public ActionBarAPI bM(){
		return bman;
	}
	
	/**
	 * Get The Arena Manager
	 * 
	 */
	public ArenaManager aM(){
		return aman;
	}
	
	/**
	 * Returns The Software Prefix
	 * 
	 */
	public String getPfx(){
		return pfx;
	}
	
	/**
	 * Returns The Status Window
	 * 
	 */
	public StatusWindow getSt(){
		return st;
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}