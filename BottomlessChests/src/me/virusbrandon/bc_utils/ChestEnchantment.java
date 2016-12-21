package me.virusbrandon.bc_utils;

public class ChestEnchantment implements java.io.Serializable{
	private static final long serialVersionUID = 84938989L;
	private int ENCHANTMENT_ID;
	private int ENCHANTMENT_LEVEL;
	
	/**
	 * ChestEnchantment Constructor
	 * 
	 * @param ID
	 * 
	 * 
	 */
	public ChestEnchantment(int ID){
		this.ENCHANTMENT_ID = ID;
	}
	
	/**
	 * ChestEnchantment Secondary Constructor
	 * 
	 * Used Only If The Enchantment Level Is Provided...
	 * Which It Is When A Chest Is Being Loaded In
	 * 
	 * 
	 */
	public ChestEnchantment(int ID, int LEVEL){
		this.ENCHANTMENT_ID = ID;
		this.ENCHANTMENT_LEVEL = LEVEL;
	}
	
	/**
	 * Returns The Enchantment ID
	 * 
	 * @return
	 * 
	 * 
	 */
	public int getId(){
		return ENCHANTMENT_ID;
	}
	
	/**
	 * Returns The Enchantment Level
	 * 
	 * @return
	 * 
	 * 
	 */
	public int getLevel(){
		return ENCHANTMENT_LEVEL;
	}
	
	/**
	 * Allows For The Level Of The Enchantment
	 * To Be Set Here Because It Was Not Set In
	 * The Constructor
	 * 
	 * @param ENCHANTMENT_LEVEL
	 * 
	 * 
	 */
	public void setLevel(int ENCHANTMENT_LEVEL){
		this.ENCHANTMENT_LEVEL = ENCHANTMENT_LEVEL;
	}
	
	/**
	 * ChestEnchantment Equals Method
	 * 
	 * 
	 */
	public boolean equals(ChestEnchantment other){
		if(getId()!=other.getId()){
			return false;
		}
		if(getLevel()!=other.getLevel()){
			return false;
		}
		return true;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
