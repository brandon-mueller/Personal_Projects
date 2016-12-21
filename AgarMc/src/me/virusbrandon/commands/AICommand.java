package me.virusbrandon.commands;

import java.util.StringTokenizer;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.virusbrandon.agarmc.Main;
import me.virusbrandon.agarui.AIUI;

public class AICommand implements Listener{
	
	@EventHandler
	public void cmd(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		if(main.findPlayer(p)!=null){
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
				if(args[0].equalsIgnoreCase("AI")){
					if(!p.hasPermission("AgarMC.Admin")){
						p.sendMessage(main.pfx()+"AI Command Rejected - No Permission!");
						return;
					}
					if(main.gB().isReady()){
						if(main.gB().getMaxAIs()<=0){
							p.sendMessage(main.pfx()+"AIs Are Disabled In The Config ( Max = 0 )");
							return;
						}
						try{
							if(args[1].equalsIgnoreCase("SHOW")){
								p.openInventory(AIUI.getInstance(main));
								return;
							}
						}catch(Exception e1){}
						if(args.length < 3){
							help(p);
							return;
						}
						if(args[1].equalsIgnoreCase("CREATE")){
							int i = 1;
							if(args[2]!=null){
								i = Math.abs(Integer.parseInt(args[2]));
							}
							for(int x=0;x<i;x++){
								if(main.getAllAIs().size()<main.gB().getMaxAIs()){
									main.createAI();
								} else {
									p.sendMessage(main.pfx()+"Config AI Limit Reached");
								}
							}
							return;
						} else if(args[1].equalsIgnoreCase("Locate")){
							if(main.locateAI(Integer.parseInt(args[2]),p)?true:help(p)){}
							return;
						} else if(args[1].equalsIgnoreCase("SPLIT")){
							if(main.splitAI(Integer.parseInt(args[2]))?true:help(p)){}
							return;
						} else if(args[1].equalsIgnoreCase("EJECT")){
							if(main.ejectAIMass(Integer.parseInt(args[2]))?true:help(p)){}
							return;
						} else if(args[1].equalsIgnoreCase("MERGE")){
							if(main.mergeAI(Integer.parseInt(args[2]))?true:help(p)){}
							return;
						} else if(args[1].equalsIgnoreCase("KILL")){
							if(main.killAI(Integer.parseInt(args[2]))?true:help(p)){}
							return;
						} else {
							help(p);
						}
					} else {
						p.sendMessage(main.pfx()+"AI Command Rejected - Gameboard Not Ready");
					}
				}
			}catch(Exception e1){}
		}
	}
	
	private Main main;
	private static AICommand instance;
	private String ye=ChatColor.YELLOW+"",bo=ChatColor.BOLD+"";
	
	private AICommand(Main main){
		this.main = main;
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}
	
	public static AICommand getInstance(Main main){
		if(instance == null){
			instance = new AICommand(main);
		}
		return instance;
	}
	
	private boolean help(Player p){
		p.sendMessage(ye+bo+"AI COMMANDS - USAGE");
		p.sendMessage(ye+"/Agarmc AI SHOW        - SHOW AI GUI");
		p.sendMessage(ye+"/Agarmc AI CREATE { X} - CREATE X AIs");
		p.sendMessage(ye+"/Agarmc AI LOCATE {ID} - TP TO AI");
		p.sendMessage(ye+"/Agarmc AI SPLIT  {ID} - SPLIT AI CELLS");
		p.sendMessage(ye+"/Agarmc AI EJECT  {ID} - EJECT AI MASS");
		p.sendMessage(ye+"/Agarmc AI MERGE  {ID} - MERGE AI CELLS");
		p.sendMessage(ye+"/Agarmc AI KILL   {ID} - KILL AI");
		p.sendMessage("");
		return true;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */