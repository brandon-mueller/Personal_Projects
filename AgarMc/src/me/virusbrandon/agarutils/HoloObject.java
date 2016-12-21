package me.virusbrandon.agarutils;

import org.bukkit.Location;

public class HoloObject {
	private HoloManager man;
	private Hologram holo;
	private Timer timer;
	private int keepAlive;
	private boolean infinite;
	
	public HoloObject(HoloManager man, Hologram holo, int keepAlive){
		this.man = man;
		this.holo = holo;
		this.keepAlive = keepAlive;
		if(keepAlive <= 0){
			this.keepAlive = 1;
			infinite = true;
		}
		this.timer = new Timer(this);
		timer.start(1);
	}
	
	public Hologram getHolo(){
		return holo;
	}
	
	public void setInfinite(boolean inf){
		this.infinite = inf;
	}
	
	public void setLocation(Location loc){
		holo.setLocation(loc);
	}
	
	protected void tick(){
		try{
			if(!infinite){
				this.keepAlive--;
			}
			if(keepAlive<=0){
				timer.stop();
				man.remHolo(this);
			} 
		}catch(Exception e1){}
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */