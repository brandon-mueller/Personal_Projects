package me.virusbrandon.meteorshower;

import me.virusbrandon.meteor_utils.HoloManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private HoloManager hm;
	private String re=ChatColor.RED+"",bo=ChatColor.BOLD+"";
	
	@Override
	public void onEnable(){
		hm = new HoloManager();
		
	}
	
	@Override
	public void onDisable(){
		hm.remAllHolos();
	}
	
	public boolean onCommand(CommandSender se, Command c, String lbl, String[] args){
		if(lbl.equalsIgnoreCase("ms")){
			if(se instanceof Player){
				Player p = (Player)se;
				new MeteorShower(this, p.getLocation());
			} else {
				se.sendMessage(re+bo+"This Command Is For In Game Players Only!");
			}
		
		}
		return true;
	}
	
	public HoloManager hM(){
		return hm;
	}
	
}
