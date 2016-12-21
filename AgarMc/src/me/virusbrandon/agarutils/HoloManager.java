package me.virusbrandon.agarutils;

import java.util.ArrayList;

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
	protected HoloObject addHolo(Hologram holo){
		HoloObject h = new HoloObject(this,holo,((int)(10+(Math.random()*30))));
		holos.add(h);
		return h;
	}
	
	/**
	 * Add Hologram With Custom Timeout
	 * @param holo
	 * 
	 */
	public HoloObject addHolo(Hologram holo, int time){
		HoloObject h = new HoloObject(this,holo,time);
		holos.add(h);
		return h;
	}
	
	protected void remHolos(ArrayList<HoloObject> ob){
		for(HoloObject o:ob){
			o.getHolo().remove();
			holos.remove(o);
		}
	}
	
	public void remHolo(HoloObject holo){
		holo.getHolo().remove();
		holos.remove(holo);
	}
	
	public void remAllHolos(){
		for(HoloObject holo:holos){
			holo.getHolo().remove();
		}
		holos.clear();
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */