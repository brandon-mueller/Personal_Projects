package me.virusbrandon.commands;

import java.util.StringTokenizer;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.virusbrandon.agarmc.Main;
import me.virusbrandon.agarutils.Pref;

public class TeleportCommand implements Listener{
	
	@EventHandler
	public void cmd(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(main.findPlayer(p)!=null){
			return;
		}
		boolean b = p.hasPermission("AgarMC.Admin");
		StringTokenizer st = new StringTokenizer(e.getMessage());
		String lbl = st.nextToken().replaceAll("/", "");
		if(lbl.equalsIgnoreCase("agarmc")){
			String[] args = new String[st.countTokens()!=0?st.countTokens():0];
			int c = 0;
			while(st.hasMoreTokens()){
				args[c] = st.nextToken();
				c++;
			}	
			try{
				if(args[0].equalsIgnoreCase("Start")){
					new Pref(main,p);
				} else if(args[0].equalsIgnoreCase("Lobby")){
					Location l = main.gB().getLobby();
					if(l==null){
						p.sendMessage(main.pfx()+"Teleport Rejected - Lobby Is Not Set");
						return;
					}
					p.teleport(new Location(l.getWorld(),l.getX(),l.getY()+2,l.getZ(),l.getYaw(),l.getPitch()));
				} else if(args[0].equalsIgnoreCase("Gameboard")){
					if(b){
						Location l = main.gB().getOrigin();
						if(l==null){
							p.sendMessage(main.pfx()+"Teleport Rejected - Gameboard Is Not Ready");
							return;
						}
						l = main.gB().getGameboardCenter();
						p.teleport(new Location(l.getWorld(),l.getX(),l.getY()+2,l.getZ()));
					} else {
						p.sendMessage(main.pfx()+"Teleport Rejected - No Permission!");
					}
				}
				return;
			}catch(Exception e1){}
			if(args.length < 1){
				p.sendMessage(ye+bo+"Agarmc BASE COMMAND");
				p.sendMessage(ye+"Type /Agarmc Help - For Help");
				p.sendMessage("");
			}
		}
	}
	
	private Main main;
	private static TeleportCommand instance;
	private String ye=ChatColor.YELLOW+"",bo=ChatColor.BOLD+"";
	
	private TeleportCommand(Main main){
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	public static TeleportCommand getInstance(Main main){
		if(instance == null){
			instance = new TeleportCommand(main);
		}
		return instance;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */