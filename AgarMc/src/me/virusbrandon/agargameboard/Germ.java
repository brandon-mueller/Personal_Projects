package me.virusbrandon.agargameboard;

import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import me.virusbrandon.agarmc.Main;

public class Germ {
	
	private Main main;
	private Location loc;
	private double radius = 2.0;
	private ArrayList<Location> locs = new ArrayList<>();
	
	public Germ(Main main){
		this.main = main;
		this.loc = main.gB().rndLoc();
		dispGerm();
	}
	
	public Germ(Main main, Location loc){
		this.main = main;
		this.loc = loc;
		dispGerm();
	}
	
	public Location getLocation(){
		return loc;
	}
	
	public double getRadius(){
		return radius;
	}
	
	
	/**
	 * Removes The Germ
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void remGerm(){
		int[] i = main.gB().getBoardBlock();
		for(Location l:locs){
			main.gB().getAllGerms().remove(((int)l.getX())+""+((int)l.getZ()));
			l.getWorld().getBlockAt(l).setTypeId(i[0]);
			l.getWorld().getBlockAt(l).setData((byte)i[1]);
		}
		locs.clear();
	}
	
	/**
	 * Displays The Germ
	 * 
	 */
	private boolean dispGerm(){
		for(int x = (int)(loc.getX()-radius),z = (int)(loc.getZ()-radius);z < (int)(loc.getZ()+radius);x++){
			Location l = new Location(loc.getWorld(),x,loc.getY()-main.gB().getCellPadding(),z);
			if(l.distance(loc)<=radius){
				main.gB().getAllGerms().put(x+""+z,this);
				locs.add(l);
				l.getWorld().getBlockAt(l).setType(Material.GRASS);
			}
			if(x >= (int)(loc.getX()+radius)){
				x = (int)(loc.getX()-radius);
				z++;
			}
		}
		return true;
	}
	
	
	/**
	 * Moves A Germ Elsewhere, Normally
	 * After A Cell Has Fallen Victim To
	 * It.
	 * 
	 */
	public void relocate(){
		this.radius = 2.0;
		remGerm();
		this.loc = main.gB().rndLoc();
		if((main.gB().getAllGermSize()/9)<=(main.gB().getBundles())){
			dispGerm();
		}
	}
	
	/**
	 * The Germ Grows A Little When Cells
	 * Launch Mass Into It.
	 * 
	 * Eventually, The Germ Will Launch Child
	 * In Several Directions Once Enough Mass Has
	 * Been Acquired By This Germ.
	 * 
	 * If This Happens While You're Close, Think
	 * Fasts. Being Split Into Small Pieces
	 * Could Cause You To Attract... Unwanted
	 * Attention.
	 * 
	 */
	public boolean grow(Vector v){
		remGerm();
		radius+=.2;
		if(radius >=4) {
			explode(v);
			radius = 2.0;
		} else {
			loc.getWorld().playSound(loc, Sound.ITEM_BUCKET_EMPTY_LAVA, 2, .5f);
		}
		return dispGerm();
	}
	
	/**
	 * This Is When The Germ Launches A Child
	 * Germ Out In The Direction Of The Last
	 * Piece Of Masss That Was Launched At It.
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void explode(Vector v){
		FallingBlock fb = loc.getWorld().spawnFallingBlock(loc,Material.GRASS,(byte)0);
		fb.setVelocity(v.multiply(2));
	    fb.setDropItem(false);
	    loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_DEATH, 2, .5f);
	}
}

/*
 * © 2016 Brandon Mueller
 * DO NOT DE-COMPILE THIS SOFTWARE OR ATTEMPT ANY FORM OF REVERSE ENGINEERING!
 */
