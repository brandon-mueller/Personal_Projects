package me.virusbrandon.agarutils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class Hologram {
	private Location l;
	private String text = "";
	private ArmorStand st;
	
	public Hologram(Location l, String text){
		this.l=l;
		this.text = text;
		st = l.getWorld().spawn(l, ArmorStand.class);
		st.setVisible(false);
		st.setSmall(true);
		st.setGravity(false);
		if(text.length()>0){
			st.setCustomName(text);
			st.setCustomNameVisible(true);
		}
	}
	
	public void remove(){
		st.remove();
	}
	
	private void moveTo(Location to){
		st.teleport(to);
	}
	
	public void setLocation(Location l){
		this.l = l;
		moveTo(l);
	}
		
	public Location getLocation(){
		return l;
	}
	
	public void setText(String text){
		this.text = text;
		st.setCustomName(text);
	}
	
	public String getText(){
		return text;
	}
}
