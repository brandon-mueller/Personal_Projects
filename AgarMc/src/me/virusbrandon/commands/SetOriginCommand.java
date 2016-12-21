package me.virusbrandon.commands;

import java.util.StringTokenizer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.virusbrandon.agarmc.Main;

public class SetOriginCommand implements Listener{
	
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
				if(args[0].equalsIgnoreCase("setOrigin")){
					try{
						p.sendMessage(main.pfx()+main.gB().setOri(new Location(Bukkit.getWorld(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4]))));
					}catch(Exception e1){
						p.sendMessage(ye+"SET ORIGIN - USAGE");
						p.sendMessage(ye+"/Agarmc setOrigin {WORLD} {X} {Y} {Z}");
						p.sendMessage("");
					}
				}
			}catch(Exception e1){}
		}
	}
	
	private Main main;
	private static SetOriginCommand instance;
	private String ye = ChatColor.YELLOW+"";
	
	private SetOriginCommand(Main main){
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	public static SetOriginCommand getInstance(Main main){
		if(instance == null){
			instance = new SetOriginCommand(main);
		}
		return instance;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */