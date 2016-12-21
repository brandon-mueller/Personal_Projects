package me.virusbrandon.meteor_utils;

import java.util.ArrayList;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;

public class HoloManager {
	private ArrayList<HoloObject> holos = new ArrayList<>();
	
	public HoloManager(){
		
	}
	
	/**
	 * Add A Hologram Display To The
	 * Hologram Manager, Which Will Disappear
	 * After A Short Time.
	 * 
	 * @param holo
	 */
	public HoloObject addHolo(Hologram holo,boolean fancy){
		HoloObject h = new HoloObject(this,holo,((int)(10+(Math.random()*30))),fancy);
		holos.add(h);
		return h;
	}
	
	/**
	 * Add Hologram With Custom Timeout
	 * @param holo
	 * 
	 */
	public HoloObject addHolo(Hologram holo, int time, boolean fancy){
		HoloObject h = new HoloObject(this,holo,time,fancy);
		holos.add(h);
		return h;
	}
	
	protected void remHolo(HoloObject holo){
		HologramAPI.removeHologram(holo.getHolo());
		holos.remove(holo);
	}
	
	public void remAllHolos(){
		for(HoloObject holo:holos){
			HologramAPI.removeHologram(holo.getHolo());
		}
		holos.clear();
	}
/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */
}