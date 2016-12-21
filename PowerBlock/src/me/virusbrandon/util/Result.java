package me.virusbrandon.util;

import org.bukkit.ChatColor;

public class Result {
	private String name;
	private String re=ChatColor.RED+"",gr=ChatColor.GREEN+"";
	private double balance;
	
	public Result(String name,double balance){
		this.name = name;
		this.balance = balance;
	}
	
	public String name(){
		return name;
	}
	
	public double balance(){
		return balance;
	}
	
	public String toString(){
		return gr+"Balance For "+name()+": $"+re+balance();
	}
}
