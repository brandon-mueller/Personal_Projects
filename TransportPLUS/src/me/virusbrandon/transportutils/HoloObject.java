package me.virusbrandon.transportutils;

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
		this.timer = new Timer(this);
		timer.start(1);
		holo.spawn();
	}
	
	public Hologram getHolo(){
		return holo;
	}
	
	protected void tick(){
		this.keepAlive--;
		if(keepAlive<=0){
			timer.stop();
			man.remHolo(this);
		}
	}
}
