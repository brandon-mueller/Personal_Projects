package me.virusbrandon.commands;

import java.util.StringTokenizer;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.virusbrandon.agarmc.Main;

public class SetLobbyCommand implements Listener{
	
	@EventHandler
	public void cmd(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(main.findPlayer(p)!=null){
			return;
		}
		if(!p.hasPermission("AgarMC.Admin")){
			return;
		}
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
				if(args[0].equalsIgnoreCase("setLobby")){
					try{
						Location l = p.getLocation();
						p.sendMessage(main.pfx()+main.gB().setLobby(new Location(l.getWorld(),l.getX(),l.getY()-1,l.getZ(),l.getYaw(),l.getPitch())));
					}catch(Exception e1){}
				}
			}catch(Exception e1){}
		}
	}
	
	private Main main;
	private static SetLobbyCommand instance;
	
	private SetLobbyCommand(Main main){
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	public static SetLobbyCommand getInstance(Main main){
		if(instance == null){
			instance = new SetLobbyCommand(main);
		}
		return instance;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */