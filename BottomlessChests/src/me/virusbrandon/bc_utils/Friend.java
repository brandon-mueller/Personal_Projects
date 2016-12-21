package me.virusbrandon.bc_utils;

import org.bukkit.entity.Player;

public class Friend {
	private String FRIEND_NAME;
	private String FRIEND_UUID;
	
	/**
	 * Friend Constructor
	 * 
	 * @param p
	 * 
	 * 
	 */
	public Friend(Player p){
		this.FRIEND_NAME = p.getName();
		this.FRIEND_UUID = p.getUniqueId().toString();
	}
	
	/**
	 * Friend Constructor - From Config
	 * 
	 * 
	 */
	public Friend(String FRIEND_NAME, String FRIEND_UUID){
		this.FRIEND_NAME = FRIEND_NAME;
		this.FRIEND_UUID = FRIEND_UUID;
	}
	
	/**
	 * Returns The Friend's Name
	 * 
	 * 
	 */
	public String getName(){
		return FRIEND_NAME;
	}
	
	/**
	 * Returns The Friend's UUID
	 * 
	 * 
	 */
	public String getUUID(){
		return FRIEND_UUID;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
