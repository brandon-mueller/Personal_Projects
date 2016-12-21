package me.virusbrandon.agarutils;

import me.virusbrandon.agarmc.Main;
import me.virusbrandon.agarui.SelectionsMenu;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Pref {
	
	private String cellColor = "";
	private boolean nameOn = true;
	private boolean random = false;
	private SelectionsMenu men; 
	private Main main;
	
	public Pref(Main main,Player p){
		this.main = main;
		if(p != null){
			men = new SelectionsMenu(this,p);
			p.openInventory(men.getUI());
		}
	}
	
	public Main getMain(){
		return main;
	}
	
	public Pref setCellColor(String cellColor){
		this.cellColor = (cellColor.equalsIgnoreCase("ANY")?ChatColor.values()[(int)(Math.random()*15)].name():cellColor);
		if(cellColor.equalsIgnoreCase("Random")){
			setRandom(true);
		}
		return this;
	}
	
	public String getCellColor(){
		return cellColor+"";
	}
	
	public Pref setNameOn(boolean nameOn){
		this.nameOn = nameOn;
		return this;
	}
	
	public boolean isNameOn(){
		return nameOn;
	}
	
	public Pref setRandom(boolean random){
		this.random = random;
		return this;
	}
	
	public boolean isRandom(){
		return random;
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */