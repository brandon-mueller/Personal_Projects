package me.virusbrandon.HoloBeam;

import me.virusbrandon.ui.ColorChooser;

import org.bukkit.entity.Player;

public class Profile {
	private String uuid;
	private String name;
	private ColorChooser ch;
	
	public Profile(Player p, Main main){
		this.uuid = p.getUniqueId().toString();
		this.name = p.getName();
		ch = new ColorChooser(main);
	}
	
	public String name(){
		return name;
	}
	
	public String uuid(){
		return uuid;
	}
	
	public ColorChooser gui(){
		return ch;
	}
}
