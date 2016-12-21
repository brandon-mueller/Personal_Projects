package me.virusbrandon.bc_utils;

import org.bukkit.Color;

/**
 * EffectColor Object,
 * Annoymous Inner Class
 * 
 * @author Brandon
 *
 *
 */
public class EffectColor{
	private int[] color; /* R - G - B */
	
	/**
	 * EffectColor Constructor
	 * 
	 * @param color
	 * 
	 * 
	 */
	public EffectColor(Color color){
		this.color = new int[]{color.getRed(),color.getGreen(),color.getBlue()};
	}
	
	/**
	 * Returns The Numerial Values Of An
	 * Effect Color
	 * 
	 * @return
	 * 
	 * 
	 */
	public int[] getColorInfo(){
		return color;
	}
	
	/**
	 * EffectColor equals Method
	 * 
	 * 
	 */
	public boolean equals(EffectColor other){
		if(getColorInfo()[0]!=other.getColorInfo()[0]){
			return false;
		}
		if(getColorInfo()[1]!=other.getColorInfo()[1]){
			return false;
		}
		if(getColorInfo()[2]!=other.getColorInfo()[2]){
			return false;
		}
		return true;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}