package me.virusbrandon.bc_utils;

import java.util.ArrayList;

import org.bukkit.*;

public class ChestFireworkEffect {
	private boolean HAS_FLICKER;
	private boolean HAS_TRAIL;
	private String EFFECT_TYPE;
	private ArrayList<EffectColor> EFFECT_COLORS = new ArrayList<>();
	private ArrayList<EffectColor> FADE_COLORS = new ArrayList<>();
	
	/**
	 * ChestFireworkEffect Constructor
	 * 
	 * @param effect
	 * 
	 * 
	 */
	public ChestFireworkEffect(FireworkEffect effect){
		this.HAS_FLICKER = effect.hasFlicker();
		this.HAS_TRAIL = effect.hasTrail();
		this.EFFECT_TYPE = effect.getType().name();
		for(Color color:effect.getColors()){
			EFFECT_COLORS.add(new EffectColor(color));
		}
		for(Color color:effect.getFadeColors()){
			FADE_COLORS.add(new EffectColor(color));
		}
	}
	
	/**
	 * ChestFireworkEffect Default Constructor
	 * 
	 * 
	 */
	public ChestFireworkEffect(){}
	
	/**
	 * Returns Whether The Effect
	 * Has Flicker
	 * 
	 * 
	 */
	public boolean hasFlicker(){
		return HAS_FLICKER;
	}
	
	/**
	 * Sets Whehter The Effect
	 * Has Flicker
	 * 
	 * 
	 */
	public void setFlicker(boolean HAS_FLICKER){
		this.HAS_FLICKER = HAS_FLICKER;
	}
	
	/**
	 * Returns Whether The Effect
	 * Has A Trail
	 * 
	 * 
	 */
	public boolean hasTrail(){
		return HAS_TRAIL;
	}
	
	/**
	 * Sets Whether The Effect
	 * Has A Trail
	 * 
	 * 
	 */
	public void setTrail(boolean HAS_TRAIL){
		this.HAS_TRAIL = HAS_TRAIL;
	}
	
	/**
	 * Returns The Name Of The
	 * Effect Type
	 * 
	 * 
	 */
	public String getEffectType(){
		return EFFECT_TYPE;
	}
	
	/**
	 * Sets The Name Of The
	 * Effect Type
	 * 
	 * 
	 */
	public void setEffectType(String EFFECT_TYPE){
		this.EFFECT_TYPE = EFFECT_TYPE;
	}
	
	/**
	 * Returns The Effect Colors
	 * 
	 * 
	 */
	public ArrayList<EffectColor> getEffectColors(){
		return EFFECT_COLORS;
	}
	
	/**
	 * Sets The Effect Colors
	 * 
	 * 
	 */
	public void setEffectColors(ArrayList<EffectColor> EFFECT_COLORS){
		this.EFFECT_COLORS = EFFECT_COLORS;
	}
	
	/**
	 * Returns The Fade Colors
	 * 
	 * 
	 */
	public ArrayList<EffectColor> getFadeColors(){
		return FADE_COLORS;
	}
	
	/**
	 * Sets The Fade Colors
	 * 
	 * 
	 */
	public void setFadeColors(ArrayList<EffectColor> FADE_COLORS){
		this.FADE_COLORS = FADE_COLORS;
	}
	
	/**
	 * ChestFireworkEffect Equals Method
	 * 
	 * 
	 */
	public boolean equals(ChestFireworkEffect other){
		if(!hasFlicker()==other.hasFlicker()){
			Bukkit.broadcastMessage("1");
			return false;
		}
		if(!hasTrail()==other.hasTrail()){
			Bukkit.broadcastMessage("2");
			return  false;
		}
		if(!getEffectType().equals(other.getEffectType())){
			Bukkit.broadcastMessage("3");
			return false;
		}
		if(getEffectColors().size()==other.getEffectColors().size()){
			for(int x=0;x<getEffectColors().size();x++){
				if(!getEffectColors().get(x).equals(other.getEffectColors().get(x))){
					Bukkit.broadcastMessage("4");
					return false;
				}
			}
		}else{
			return false;
		}
		if(getFadeColors().size()==other.getFadeColors().size()){
			for(int x=0;x<getFadeColors().size();x++){
				if(!getFadeColors().get(x).equals(other.getFadeColors().get(x))){
					Bukkit.broadcastMessage("5");
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}
