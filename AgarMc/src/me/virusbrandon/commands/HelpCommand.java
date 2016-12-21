package me.virusbrandon.commands;

import java.util.StringTokenizer;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.virusbrandon.agarmc.Main;
import me.virusbrandon.agarutils.AutomatedSetup;

public class HelpCommand implements Listener{
	
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
				if(args[0].equalsIgnoreCase("Help")){
					p.sendMessage(ye+bo+"AGARMC - HELP - COMMANDS");
					p.sendMessage(ye+"Run Commands Below To See USAGE Or To Run");
					p.sendMessage("");
					p.sendMessage(ye+"/Agarmc Start");
					p.sendMessage(ye+"/Agarmc Lobby");
					if(b){
						p.sendMessage(ye+"/Agarmc Gameboard");
						p.sendMessage(ye+"/Agarmc setLobby");
						p.sendMessage(ye+"/Agarmc setOrigin");
						p.sendMessage(ye+"/Agarmc FullReset");
						p.sendMessage(ye+"/Agarmc AI");
					}
					p.sendMessage(ye+"/Agarmc Help");
					p.sendMessage("");
				}
				if(args[0].equalsIgnoreCase("FullReset") && b){
					new AutomatedSetup(main,false).destroyEverything();
				}
			}catch(Exception e1){}
		}
	}
	
	private static HelpCommand instance;
	private String ye=ChatColor.YELLOW+"",bo=ChatColor.BOLD+"";
	private Main main;
	
	private HelpCommand(Main main){
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	public static HelpCommand getInstance(Main main){
		if(instance == null){
			instance = new HelpCommand(main);
		}
		return instance;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */