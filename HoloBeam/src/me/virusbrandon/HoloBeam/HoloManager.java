package me.virusbrandon.HoloBeam;

import java.util.ArrayList;

import de.inventivegames.hologram.Hologram;



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
		holos.add(new HoloObject(this,holo,((int)(Math.random()*10))));
	}
	
	
	/**
	 * Remove A Hologram Display After A Short Time
	 * @param holo
	 */
	protected void remHolo(HoloObject holo){
		holo.kill();
		holos.remove(holo);
	}
	
	/**
	 * Removes All Holos
	 * 
	 */
	protected void clearHolos(){
		while(holos.size()>0){
			holos.get(0).kill();
			holos.remove(0);
		}
	}	
}
