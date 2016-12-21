package me.virusbrandon.laser;

import me.virusbrandon.game.Arena;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.inventivegames.hologram.HologramAPI;

public class Laser {
	private Arena a;
	
	public Laser(Arena a){
		this.a = a;
	}
	
	/**
	 * Generates HoloBeam, STAND CLEAR!
	 * 
	 * @param loc
	 * @param d
	 * @param bC
	 * @param bL
	 */
	public void beam(Location loc,Vector d,String bC, int bL,Player p){
		if(bL>0){
			a.getMan().getMain().hM().addHolo(HologramAPI.createHologram(loc,bC),false);
			dmgEnt(loc,p,bL);
			Location l = new Location(loc.getWorld(),(loc.getX()+(d.getX()*.3)),(loc.getY()+(d.getY()*.3)),(loc.getZ()+(d.getZ()*.3)));
			l.getWorld().playSound(l,Sound.ENTITY_HORSE_HURT, .5f, 2f);
			beam(l,d,bC,bL-1,p);
		}
	}
	
	/**
	 * Entities Ignite If To Close To
	 * Extremely Dangerous Holo Beam
	 * 
	 * @param loc
	 */
	private void dmgEnt(Location loc,Player p,int bL){
		for(Entity e:loc.getWorld().getEntities()){
			double d = (a.getMan().calcDist(loc,e.getLocation()));
			if((d<2)&&(!e.getUniqueId().equals(p.getUniqueId()))){
				try{
					Player pp = (Player)e;
					loc.getWorld().createExplosion(loc, 1);
					a.findPlayer(pp).dmg(10, p);
					
				}catch(Exception e1){}
			}
		}
	}
	/*
	 * © 2016 Brandon Mueller
	 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
	 */
}