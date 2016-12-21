package me.virusbrandon.meteor_utils;

import org.bukkit.ChatColor;

import de.inventivegames.hologram.Hologram;

public class HoloObject {
	private HoloManager man;
	private Hologram holo;
	private Timer timer;
	private int keepAlive;
	private boolean fancy;
	private int cCn = 0;
	private String[] c = new String[]{"GREEN","AQUA","RED","YELLOW","GOLD"};
	
	public HoloObject(HoloManager man, Hologram holo, int keepAlive, boolean fancy){
		this.man = man;
		this.holo = holo;
		this.keepAlive = keepAlive;
		this.fancy = fancy;
		this.timer = new Timer(this);
		timer.start(1);
		holo.spawn();
	}
	
	public Hologram getHolo(){
		return holo;
	}
	
	protected void tick(){
		try{
			this.keepAlive--;
			if(keepAlive<=0){
				timer.stop();
				man.remHolo(this);
			} else if(fancy){
				cCn++;
				cCn=(cCn%c.length);
				String tR = "";
				if(cCn==0){
					tR=ChatColor.valueOf(c[c.length-1])+"█";
				} else {
					tR=ChatColor.valueOf(c[cCn-1])+"█";
				}
				String s = holo.getText().replaceAll(tR, ChatColor.valueOf(c[cCn])+"█");
				s += s.substring(0,3);
				s = s.substring(3,s.length());
				holo.setText(s);
			}
		}catch(Exception e1){}
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}