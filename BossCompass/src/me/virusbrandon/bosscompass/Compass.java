package me.virusbrandon.bosscompass;

import me.virusbrandon.localapis.ActionBarAPI;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Compass {
	private String[] dir = new String[]{"S","W","N","E"};
	private String disp = ""; 			/* Compass Display */
	private String template = "☗╻▍";		/* Compass Template */
	private String bo=ChatColor.BOLD+"",wh=ChatColor.WHITE+"",bl=ChatColor.BLACK+"",res=ChatColor.RESET+"",ye=ChatColor.YELLOW+"",aq=ChatColor.AQUA+"",bu=ChatColor.BLUE+"",gr=ChatColor.GREEN+"",ga=ChatColor.GRAY+"",da=ChatColor.DARK_GRAY+"";
	private String[] clrs = new String[]{gr,bu,ga};
	
	/**
	 * The Compass Constructor.
	 * 
	 */
	public Compass(){
		for(int x=0;x<dir.length;x++){
			disp+=dir[x]+" ╻ ▍ ╻ ▍ ╻ ▍  ☗  ▍ ╻ ▍ ╻ ▍ ╻ ";
		}
	}
	
	/**
	 * Builds And Sends The Compass
	 * View Out To A Player's Actionbar
	 * 
	 * @param p
	 * @param bar
	 */
	public void sendCompass(Player p, ActionBarAPI bar){
		int c = 100;
		Location ll = p.getLocation();
		float i = ll.getYaw();
		i = (i<0)?i+360:i; // Wierd Bug In Bukkit With Yaw Calc :/
		int l = (int)((((i-c)<0)?((i-c)+360):(i-c))/3);
		int r = (int)((((i+c)>360)?((i+c)-360):(i+c))/3);
		clrs[1]=(isNight(ll)?ye:aq);
		clrs[2]=(isNight(ll)?ga:da);
		String s = compPrep(((l>r)?disp.substring(l, disp.length())+disp.substring(0, r):disp.substring(l, r)));
		for(String ss:dir){
			s = s.replaceAll(ss,(isNight(ll)?wh+bo:bl+bo)+ss+res);
		}
		bar.sendActionBar(p,s);
	}
	
	/**
	 * Prepares The Compass View.
	 * 
	 * @param s
	 * @return
	 */
	private String compPrep(String s){
		for(int x=0;x<template.length();x++){
			s = s.replaceAll(template.substring(x,x+1), clrs[x]+template.substring(x,x+1));
		}
		return s;
	}
	
	/**
	 * Checks If It's Night Time In The
	 * Player's Current World.
	 * 
	 * @param l
	 * @return
	 */
	private boolean isNight(Location l){
		long time = l.getWorld().getTime();
		return (time>13000&&time<23000)?true:false;
	}
}
