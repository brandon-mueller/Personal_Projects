package me.virusbrandon.transportutils;

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
	public void addHolo(Hologram holo){
		holos.add(new HoloObject(this,holo,((int)(10+(Math.random()*30)))));
	}
	
	/**
	 * Add Hologram With Custom Timeout
	 * @param holo
	 * 
	 */
	public void addHolo(Hologram holo, int time){
		holos.add(new HoloObject(this,holo,time));
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
	
}
