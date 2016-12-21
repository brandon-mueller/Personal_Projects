package me.virusbrandon.HoloBeam;

import de.inventivegames.hologram.Hologram;

public class HoloObject {
	private HoloManager man;
	private Hologram holo;
	private Timer timer;
	private int keepAlive;
	
	public HoloObject(HoloManager man, Hologram holo, int keepAlive){
		this.man = man;
		this.holo = holo;
		this.keepAlive = keepAlive;
		this.timer = new Timer(this,0);
		timer.start(1);
		holo.spawn();
	}
	
	/**
	 * The Tick Function:
	 * 
	 * Controls How Much Longer This
	 * HoloBeam Will Remain Intact
	 * 
	 */
	protected void tick(){
		this.keepAlive--;
		if(keepAlive<=0){
			timer.stop();
			man.remHolo(this);
		}
	}
	
	/**
	 * Returns This Instance Of The HoloObject
	 * @return
	 */
	protected Hologram getHolo(){
		return holo;
	}
	
	/**
	 * Destroys This Current HoloBeam Structure
	 * 
	 * @return
	 */
	protected HoloObject kill(){
		timer.stop();
		holo.despawn();
		return this;
	}
}
